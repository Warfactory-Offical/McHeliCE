#!/bin/sh

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <mappings.srg> <target_directory>"
  exit 1
fi

srg_file="$1"
target_dir="$2"

if [ ! -f "$srg_file" ]; then
  echo "SRG file not found: $srg_file"
  exit 2
fi

if [ ! -d "$target_dir" ]; then
  echo "Target directory not found: $target_dir"
  exit 3
fi

# Apply substitutions using awk
find "$target_dir" -type f -name '*.java' | while read -r file; do
  awk -v srg="$srg_file" '
    BEGIN {
      # Load valid mappings into array
      while ((getline < srg) > 0) {
        if ($1 == "MD:") {
          split($3, a, "/"); obf = a[length(a)];
          split($4, b, "/"); deobf = b[length(b)];
        } else if ($1 == "FD:") {
          split($2, a, "/"); obf = a[length(a)];
          split($3, b, "/"); deobf = b[length(b)];
        } else {
          next;
        }

        # Only accept clean Java identifiers
        if (obf ~ /^[a-zA-Z_][a-zA-Z0-9_]*$/ && deobf ~ /^[a-zA-Z_][a-zA-Z0-9_]*$/) {
          map[obf] = deobf;
        }
      }
      close(srg)
    }
    {
      for (k in map) {
        gsub("\\<" k "\\>", map[k]);
      }
      print;
    }
  ' "$file" > "$file.tmp" && mv "$file.tmp" "$file"
done

echo "Substitutions complete (safe mode, only valid Java identifiers)."

