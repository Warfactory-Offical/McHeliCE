
2017/08/17


; ★ 重要 ★
; 設定ファイル と モデル はマインクラフトを起動したままでも再読み込みできる。
; [ 機体に乗る → Rキーで補給画面 → MOD Option → Development → Reload aircraft setting ]
; テクスチャ,音声の再読み込みはヘリMODからではなくマインクラフトのデフォルトの機能を利用する。
; [ Escキーでゲームメニュー → 設定 → リソースパック → 完了 ]

;***********************************************************************************
■ヘリコプター/戦闘機/地上兵器/車両の設定ファイル 共通設定
;***********************************************************************************

DisplayName =  AH-6 Killer Egg
;表示される名前 ※全角を使用しないこと、半角英数字と記号のみ

AddDisplayName = ja_JP, AH-6 キラ－エッグ
;アイテムを持った際に表示される名前
; ※日本語全角を使用する際はファイルの文字コードをUTF-8にすること

ItemID = 28801
;アイテムID (Minecraft内では +256 されて使用される)
; ※ ItemIDは 1.7.2以降使用しないが、1.6.4以前にも対応する場合には設定が必要

Invulnerable = true
; 機体が無敵になる。
; マルチプレイで拠点の防衛兵器などに使用するとよい。

CreativeOnly = true
; クリエイティブのプレイヤーしか設置やアイテム化ができなくなる。

AddTexture = sh60-us-1
AddTexture = sh60-jp-1
AddTexture = sh60-jp-2
;追加テクスチャ (複数可)
; デフォルトでは設定ファイル名と同じ名前のpngが使われるが、
; それ以外の追加のテクスチャを追加する(拡張子は不要)。

ThirdPersonDist = 12
;3人称視点時のデフォルト距離を指定する。
;ヘリMODではPageUp/Downで4ブロック分距離を変更するため、4の倍数を推奨

CameraPosition = 0.0, 1.1, 3.7
CameraPosition = 0.0, 1.1, 3.7, false
CameraPosition = 0.0, 1.1, 3.7, true, 30, 45
;カメラの座標
;複数設定すると、Hキーでそれぞれ視点を変えられる
;1,2,3番目 座標
;4番目の設定をtrueにすると常にカメラからの視点になる
;5番目 横方向の角度
;6番目 縦方向の角度

CameraZoom = 3
; カメラのズームの最大

HUD = heli, heli_gnr, none, gunner
; 座席ごとに使用するHUD設定ファイル名
; 例の場合、パイロットは heli.txt、2番席が heli_gnr.txt、3番はHUD無し、4番席が gunner.txt になる。
; また、座席数より少なく設定すると、省略された座席は以下のデフォルト設定になる。
; この設定が無い場合、デフォルトで以下の設定使用される。
; ヘリコプター：HUD = heli,    heli_gnr, gunner, gunner, gunner, gunner ...
; 固定翼機　　：HUD = plane,   plane,    gunner, gunner, gunner, gunner ...
; 地上兵器　　：HUD = vehicleのみ
;
; ※ パイロット席のみ、ガンナーモード中は2番席のHUD設定を使う
;    1人乗りの機体でも、ガンナーモードが有効な機体は2番席も設定すること。
;    HUD = heli, heli_gnr

EnableGunnerMode = true
;ガンナーモード切り替えの有無(true=有り、false無し)

EnableNightVision = true
;ナイトビジョン切り替えの有無(true=有り、false無し)

EnableEntityRadar = false
;レーダーの有無(true=有り、false無し)

Speed = 0.6
;機体の速度 大きいほど早い

MotionFactor = 0.96
; 機体の移動速度の減速値。範囲は 0.0～1.0 で小さいほど減速が強くなりスピードも下がる。
; デフォルトは 0.96 で少し値を変えるだけでも効果がある。

Gravity = -0.04
; 機体にかかる重力を設定する
; 負の値で下に落ちる

GravityInWater = -0.04
; 水中での機体にかかる重力を設定する
; 負の値で下に落ちる


StepHeight = 2.5
; 乗り越えられるブロックの高さを指定する


MobilityYaw
MobilityYawOnGround
;機体の横方向の回転量、大きいほど機動性が良い
; MobilityYawOnGround は地上のみ影響する。水上では影響しない。
MobilityRoll
;機体のロールの回転量、大きいほど速くロールできる
MobilityPitch
;機体の縦方向の回転量、大きいほど機動性が良い、地上兵器は仰角の上下限設定になる
MinRotationPitch
MaxRotationPitch
;範囲 MinRotationPitch -80～ 0
;範囲 MaxRotationPitch   0～80
;機体の縦方向の回転限界(最小・最大)
;※ ヘリコプターと戦闘機はこの設定を有効にするとロールにも制限がかかる

MinRotationRoll
MaxRotationRoll
;機体のロールの回転限界(最小・最大)
;範囲 MinRotationRoll -80～ 0
;範囲 MaxRotationRoll   0～80
;※ ヘリコプターと戦闘機はこの設定を有効にすると縦方向の回転にも制限がかかる

UnmountPosition = 3.0, 1.0, -2.0
; 機体から降りた時の座標

AddSeat =-0.45,  0.80,  1.20
AddSeat = 0.45, -0.50,  1.20
AddSeat =-0.90, -0.50,  0.20
AddSeat = 0.90, -0.50,  0.20, true
;座席を追加する ※UAVを除き座席は必ず1つ以上なければならない
; 1つ目がパイロットの座席
; パラメータは位置(X,Y,Z)
; 4番目のパラメータはパイロットの向きに合わせて座席位置を変えるかどうか(主に戦車の砲塔)

AddGunnerSeat = -0.45, 0.80, 1.20,   0.0, 2.00, -1.01,   true
AddGunnerSeat = -0.45, 0.80, 1.20,   0.0, 2.00, -1.01,   true, -60, 78, true
; AddGunnerSeat=座席X, 座席Y, 座席Z,  カメラX, カメラY, カメラZ,  視点切り替え可,  カメラの上方向の上限(-90～0), カメラの下方向の上限(0～90), 砲塔に合わせて座席位置を変える
; 座席を追加する
; AddGunnerSeat で追加した座席のプレイヤーは視点がカメラからの視点になる
; (パイロット席に設定した場合はカメラ視点にならない)
; パラメータは座席位置(X,Y,Z)と、カメラの位置
; カメラの位置は省略可能で、省略時は CameraPosition の位置になる
; 視点切り替え可を省略か false にするとカメラの視点のみになり、true にするとHキーでプレイヤー視点に切り替え可能
; 10番目のパラメータはパイロットの向きに合わせて座席位置を変えるかどうか(主に戦車の砲塔)

AddFixRotSeat = -0.45,  0.80,  1.20, 0.0,2.00,-1.01,  true,  -50, 40
; AddGunnerSeat=座席X, 座席Y, 座席Z,  カメラX, カメラY, カメラZ,  視点切り替え可,  カメラ固定角度(横), カメラ固定角度(縦) 
; 座席を追加する
; AddGunnerSeat とほぼ同じだが、カメラが固定であることが異なる。
; カメラ固定角度を設定すると、マウス操作でカメラの位置を変えられなくなる。CtrlキーでFreeLookに変えられる。


; ★★★★★
; 機体の積み込み機能は、
; ・乗せる側が乗せる機体を指定する(AddRack)
; ・乗る側が乗る機体のラック番号を指定する(RideRack)
; のどちらかが設定されていれば有効になる。

AddRack = container,                 0.0, 1.4, -4.7,  0.0, 1.0, -16.1
AddRack = container / ah-64,         0.0, 1.4, -4.7,  0.0, 1.0, -16.1,  5.0, 20
AddRack = helicopter/vehicle / t-4,  0.0, 1.4, -4.7,  0.0, 1.0, -16.1,  5.0, 100000,  0.0, 0.0
; ■これは乗せる側の設定
; AddRack = 
;  Param1   : 乗せられるエンティティ名
;  Param2~4 : ラックの座標 X,Y,Z  機体のどこに配置するかを指定する
;  Param5~7 : ラックの出入口 X,Y,Z  乗せるエンティティがこの座標の近くにいるときに乗せることができる。降ろすときもここから。
;           : ↑例としてAC-130の場合 ラックの座標は機内 を指定し、ラックの出入口 は後部のハッチ辺りを指定する
;  Param8   : 入り口からの範囲(半径) Param8以降は省略可
;  Param9   : パラシュートを開く高度 例のように非常に大きな値にすればパラシュートを開かない
;  Param10  : 乗せたエンティティの横方向の角度
;  Param11  : 乗せたエンティティの縦方向の角度
; コンテナやヘリなどをモブのように機体に乗せることができるラックを追加する
;  エンティティ名は コンテナ、ヘリコプター、固定翼機、地上兵器、または機体名を直接指定 の組み合わせで指定する。 / で区切る
;   container  コンテナ
;   helicopter 全ヘリコプター
;   plane      全固定翼機
;   vehicle    全地上兵器
;   機体名指定 ah-64 や t-4、s-75 のように直接機体を指定する

RideRack = c5, 1
; ■これは乗る側の設定
; RideRack = 乗る機体名, ラック番号 (1～) 
; 他の機体に乗る場合に設定する。


ExclusionSeat = 15, 17
; パラメータは2つ以上ならいくつでも設定できる
; 指定した座席またはラック間の排他を設定する
; これはラックにも効果があり、あるラックにコンテナが乗っている場合は機体を乗せないなどの設定が可能になる
;
; 例えば ExclusionSeat = 3, 4, 5 とした場合
; 3番席にモブが乗っていると、4番5番席にモブが乗れなくなる
; また、4番席にモブが乗っていると、3番5番席にモブが乗れなくなる
; 同様に、5番席にモブが乗っていると、3番4番席にモブが乗れなくなる
;
; 座席/ラックの番号は、全ての座席の記載順に採番した後に残りのラックを採番する
; 例：以下のように設定ファイルに座席やラックを設定した時の番号
;  AddSeat  ←1番
;  AddRack  ←4番
;  AddGunnerSeat  ←2番
;  AddRack  ←5番
;  AddSeat  ←3番
;  AddRack  ←6番
; この例で分かる通り、全ての座席の後にラックを記載したほうが番号が分かりやすい


TurretPosition = 0.0, 0.0, 0.25
; 砲塔の回転の中心位置
; この設定の使用はあまり推奨しない。できれば砲塔の回転位置X,Z座標が0になるのが望ましい。

AddWeapon = m230,     0.00, 0.90, 2.54,   0.0, 0.0, true, 2
AddWeapon = hydra70,  0.00, 0.90, 2.54,   0.0, 0.0, true, 1, 0,-60,60, 0,25
AddWeapon = m134,     1.48, 0.40, 1.54,   1.0, 0.0
AddWeapon = m134,    -1.48, 0.40, 1.54,  -1.0, 0.0
AddTurretWeapon = hydra70,  0.00, 0.90, 2.54,   0.0, 0.0, true, 1, 0,-60,60, 0,25
;武器を追加する (weaponsフォルダの拡張子を除いたファイル名と一致すること)
; m134のように全く同じ武器を連続で登録した場合、
; 2箇所から順に使用する1つの武器として扱う
; パラメータは前から順に 武器設定ファイル名、位置(X,Y,Z)、回転角度(横,縦), パイロット使用可否, 座席, DefaultYaw, MinYaw, MaxYaw, MinPitch, MaxPitch
;
; 座席は1で1番席、2で2番席、3で3番席、4以降も同様。
; 補足 「パイロット使用可否, 座席」 の組み合わせの意味について
; true,  2 → 2番席のプレイヤーが使用可能。2番席にプレイヤーがいない時はパイロットが使用可能。
; false, 2 → 2番席のプレイヤーが使用可能。パイロットは使用不可能。
; false, 1 → パイロットのみ使用可能（非推奨）
; true,  1 → パイロットのみ使用可能
; 省略すると true, 1 になる。
;
; DefaultYaw は武器のデフォルトの向きで、モデルに合わせる
; MinYaw は、機体を上から見た時に、DefaultYawから反時計回りに動かせる角度
; MaxYaw は、機体を上から見た時に、DefaultYawから時計回りに動かせる角度
; MinPitch は、上方向に動かせる角度
; MaxPitch は、下方向に動かせる角度
;
; AddTurretWeapon のAddWeaponとの違いは砲塔の向きによって発射位置が変わる点のみ

AddSearchLight      = 0.71,  -0.02,  0.02,   0x50FFFFFF,   0x10FFFFC0,    60.0, 20.0,       0,   0
AddFixedSearchLight = 0.71,  -0.02,  0.02,   0x50FFFFFF,   0x10FFFFC0,    60.0, 20.0,       0,   0
AddSteeringSearchLight = -0.52,0.90, 1.76,   0x50FFFFFF,   0x00FFFFC0,    27.0, 15.0,       5,   0,     45
;AddSearchLight     = 座標X, 座標Y, 座標Z,   開始地点の色, 終了地点の色,  距離, 終端の半径, Yaw, Pitch, 舵角
; AddSearchLight      : 常に乗員の向く方向に角度を合わせる可動ライト
; AddFixedSearchLight : 常に同じ向きを向く、固定ライト
; AddSteeringSearchLight : 常にタイヤと同じ向きを向く、固定ライト。舵角はタイヤの舵角に合わせるとよい
; Yaw と Pitch はサーチライトの向く角度で、固定ライトでも可動ライトでも効果がある

AddPartLightHatch =  0.32, 0.23, 1.83,   -1,0,-0.024, 90
;AddPartLightHatch= 座標X, 座標Y, 座標Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 -1800～1800
; サーチライトがONの間だけ開くパーツを追加する。
; ★重要：AddSearchLight または AddFixedSearchLight が必須

AddRecipe = " Y ",  "YXY",  " YD",  X, iron_block, Y, iron_ingot, D,dye,2
AddRecipe ="YXY", X, mcheli:ah-6, Y, redstone
AddShapelessRecipe = iron_block, iron_ingot, dye,2
;レシピを追加する（AddRecipeを2行以上書くとその分レシピが増える）
; ""内の3文字はワークベンチの横一列を示す。
; (Forge の GameRegistry.addRecipe と同じフォーマット)
; 上記例の詳細
; X = 鉄Block名
; Y = 鉄インゴットのItem名
; D = 緑の染料のItem名, 染料やポーションなどのダメージ指定で切り替えるものは、Item名の後にダメージ
; Item 名は下記サイトの名前をそのまま指定すれば良い。
http://minecraft.gamepedia.com/Data_values
; 例：minecraft:iron_block など。
; バニラのアイテム/ブロックの場合、minecraft: は省略して良い
; MODのアイテムを指定する場合、 mcheli:ah-6 のようにMOD名の指定が必要
; AddShapelessRecipe で指定すると不定形レシピになる

FlareType = 1
; フレアの有無
; 0=無し
; 1=有り
; 2=大型機向けのフレア
; 3=横方向に撒く
; 4=正面に撒く
; 5=下方向に撒く
; 10=戦車用のスモークディスチャージャー

Float  = true
; 水に浮く設定

FloatOffset = -1.0
; 水に浮いた際の高さのオフセット(負の値可)

SubmergedDamageHeight = 2
; 指定した高さまでの水に触れてもダメージを受けない
; 2であれば2ブロックまでダメージ無し

MaxHP = 100
; 耐久力

ArmorDamageFactor = 0.5
; 機体が受けるダメージの係数
; 1.0 で100%、0.5で50%に軽減される

ArmorMinDamage = 5
; ダメージとして扱う最低値
; この値より小さいダメージは受けなくなる

ArmorMaxDamage = 500
; 最大ダメージ
; これより大きいダメージは、指定した値に丸められる
; 例えば 100 を指定して、300のダメージを与えようとしても、機体には100のダメージしか入らない

InventorySize = 18
; 機体のインベントリサイズ (9の倍数であること)

DamageFactor = 0.2
; プレイヤーがダメージを受けた際のダメージ係数
; 0.2を指定した場合、本来受けるダメージの 20% のダメージに減る
; この設定とは関係なく、プレイヤーが受けたダメージは機体も受ける


Sound = heli
; スロットルを上げた際になる音声ファイル名を指定
; この例では sounds/heli.ogg が読み込まれる

UAV = true
SmallUAV = true
; trueにすると機体は無人機になり、パイロット席に乗れなくなる。
; UAVステーション以外からの生成や操作ができなくなる。
; UAV      = true の場合：大型UAV扱いとなり、携帯UAV制御端末からは制御できない。
; SmallUAV = true の場合：小型UAV扱いとなり、携帯UAV制御端末から制御できる。
; 補足：UAVステーションは大きさにかかわらず全てのUAVを制御できる。
; 　　　携帯UAV制御端末は小型UAVのみ制御できる。

TargetDrone = true
; 戦闘機のみに有効。trueにすると機体は無人標的機になり、パイロット席に乗れなくなる。
; UAVステーション以外からの生成ができなくなる。
; 生成すると自動で一低高度まで飛び、旋回し続ける

OnGroundPitch = 回転角度
; 地上にいる際の仰角を指定する。
; 例えば零戦などの機体は地上にいる際は少し上を向いている

AddPartHatch = 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180
; Zキーで開閉できるハッチを追加する。
; モデルファイル名 機体名_hatch?.obj
; ? の部分は0から始まる連番
; 1つめの AddPartHatch は 機体名_hatch0.obj
; 2つめの AddPartHatch は 機体名_hatch1.obj
; このファイル名のモデルが見つからない場合、表示されない (表示が不要であれば、モデルは不要)

AddPartSlideHatch = 移動量X, 移動量Y, 移動量Z
; スライドして開くタイプのハッチを追加
; モデルファイル名(回転/スライド共通) 機体名_hatch?.obj (命名規則はAddPartHatchを参照)

AddPartCamera = 座標X,Y,Z, Yaw連動, Pitch連動
; 常にプレイヤーの方向を向くパーツを追加する。
; 2番席にモブが居る場合、2番席のモブの方向を向く。
; モデルファイル名 機体名_camera?.obj (命名規則はAddPartHatchを参照)

AddPartRotation = 0.00, 9.00, -31.17,  0,-1,0,       1.3,      false
; AddPartRotation = 位置X, Y, Z        回転軸X,Y,Z   回転速度, 常に回転させるか
; 一定間隔で回転するパーツを追加

AddPartWeapon        = m230,       false, true, true,  -2.51,  1.29,  -1.51
AddPartWeapon        = m102_105mm, false, true, true,  -2.51,  1.29,  -1.51, 1.00
AddPartWeapon        = rehinmetall_apfsds / rehinmetall_he, false, true, false,  0.00, 2.10, 0.00, 0
AddPartTurretWeapon  = mg7_62mm,   false, true, true,  -0.83,  3.39,  -0.57, 0
AddPartRotWeapon     = m134_r50,   false, true, true,  -1.825, 1.475, -0.25, 1,0,0
AddPartWeaponChild   = false, true, 0.00, 0.5, 3.00
AddPartWeaponMissile = aim120,     false, false,false, -2.51,  1.29,  -1.51
; ヘリコプター＆戦闘機用の武器パーツ設定
; AddPartWeapon = 連動する武器名(無しの時はnone), ガンナー時非表示？, Yaw連動, Pitch連動, 回転座標X,Y,Z, 駐退距離
; AddPartRotWeapon = 連動する武器名(無しの時はnone), ガンナー時非表示？, Yaw連動, Pitch連動, 回転座標X,Y,Z, 回転軸X,Y,Z
; AddPartWeaponChild = Yaw連動, Pitch連動, 回転座標X,Y,Z
; AddWeapon で追加した武器に連動して角度が変わる。武器名は / で区切って複数設定できる
; 武器の角度の範囲もAddWeaponで追加した武器に従う。
; 駐退距離は砲の駐退する距離
; AddPartRotWeapon はガトリング用で、武器使用中は回転する
; モデルファイル名 機体名_weapon?.obj (命名規則はAddPartHatchを参照)
;
; AddPartWeaponChild は AddPartWeapon の小パーツとして追加できる。
; AddPartWeapon の次の行に書くこと。
; 地上兵器の小パーツと同様複数追加することができる。
; モデルファイル名 機体名_weapon?_0.obj (?は親パーツの番号)
; 連動武器名とガンナー時非表示は親パーツと同じになる。
;
; AddPartWeaponMissile は武器使用後、使用可能になるまで非表示にする。
; 例えばミサイルや爆弾などに使用。
;
; AddPartTurretWeapon は砲塔の回転に合わせて表示位置が変わる。それ以外はAddPartWeaponと同じ


AddPartWeaponBay = 武器名, 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180
; 回転して開くタイプのウェポンベイを追加
AddPartSlideWeaponBay = 武器名, 移動量X, 移動量Y, 移動量Z
; スライドして開くタイプのウェポンベイを追加
; モデルファイル名(回転/スライド共通) 機体名_wb?.obj
; モデルファイル名 機体名_wb?.obj (命名規則はハッチを参照)

AddPartCanopy = 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180
; 回転して開くタイプのキャノピーを追加
AddPartSlideCanopy = 移動量X, 移動量Y, 移動量Z
; スライドして開くタイプのキャノピーを追加
; モデルファイル名(回転/スライド共通) 機体名_canopy?.obj
; キャノピーは複数追加できるようになった
; モデルファイル名 機体名_canopy?.obj (命名規則はハッチを参照)
; 互換性のためキャノピーのみ数値部分を省略すると、機体名_canopy0.obj として扱われる。


AddPartThrottle = 位置X, 位置Y, 位置Z,  回転軸X, 回転軸Y, 回転軸Z,  回転角度(0～180),  移動量X, 移動量Y, 移動量Z
; スロットルに連動して回転/移動するパーツを追加する
; 回転角度までは必須

AddPartLG = 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180 [, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180]
AddPartLGRev = 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180 [, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180]
AddPartSlideRotLG = 移動量X, 移動量Y, 移動量Z,  位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180
AddPartLGHatch = 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180 [, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180]
; ランディングギアを追加
; 離陸時などに自動で展開/格納
; モデルファイル名 機体名_lg?.obj (命名規則はハッチを参照)
; AddPartLGRev はAddPartLGと動作が逆転する。
; AddPartLGHatch はギアの折りたたみか展開時のみ開く。
;
; AddPartLG         ... ギア折りたたみ時 0  → 90
; AddPartLGRev      ... ギア折りたたみ時 90 → 0
; AddPartSlideRotLG ... ギア折りたたみ時 0  → 90
; AddPartLGHatch    ... ギア折りたたみ時 0  → 90 → 0

TrackRollerRot = 30
; 転輪の回転速度：0より小さい値を設定すると逆回転するはず

AddTrackRoller = -1.72,  0.77,  5.04
; 転輪を追加する、設定は座標だけで、X軸が負の値だと右側、正の値だと左側の転輪となる
; 履帯と同じ動きをするが、履帯がなくても設定可能

AddCrawlerTrack = false, 0.37, -2.09,  1.03/-3.41, 0.72/-3.57, 0.37/-3.42, -0.15/-2.55, -0.25/-2.16, -0.25/3.88, -0.13/4.21, 0.52/5.29, 0.78/5.39, 1.03/5.28, 1.10/5.04, 1.15/-3.12
;AddCrawlerTrack = 履帯の表裏逆転,  1つの履帯の間隔, 履帯のXの位置, 履帯の回転ポイントY/Z, 履帯の回転ポイントY/Z, 履帯の回転ポイントY/Z, ...
; 履帯の裏表が逆だったり、履帯がおかしな動きをするときは「履帯の表裏逆転」を trueまたはfalseにすると直ることがある。
;  補足：履帯の回転ポイントを戦車を左側面からみて時計回りに設定した時はfalse、その逆の場合はtrue。
; 転輪がなくても動作する。
; 指定した位置はゲーム内でテストモードにすると赤い点～青い点で表示される。

PartWheelRot = 40
; タイヤの回転スピード、大きいほど速い

AddPartWheel     = -1.05, 0.157, 1.965,  30
; タイヤを追加     X座標, Y座標, Z座標,  舵角
AddPartWheel     =  0.68,  0.19,  1.20,  30,   0.0, 1.0, 0.2,   0.68, 0.19, 0.70
; タイヤを追加     X座標, Y座標, Z座標,  舵角, 回転軸X,Y, Z,    回転位置X,Y,Z
; 舵角は旋回時のタイヤのY軸の最大角度
; 回転軸を省略すると(0,1,0)が使用される

AddPartSteeringWheel =  -0.54, 0.88,  0.48,   0.0,     1.0, -1.7,  130
; ハンドルを追加        X座標, Y座標, Z座標,  回転軸X, 軸Y, 軸Z,   最大回転角度

ThrottleUpDown = 1.0
ThrottleUpDownOnEntity = 2.0
; スロットルの上げ下げの係数
; 小さいほどスロットルが上がりにくくなり、離陸に時間が掛かる
;
; ThrottleUpDownOnEntity は機体が他のエンティティに乗っている時のスロットルの上げ下げ係数(デフォルト 2.0)
; 機体が他のエンティティに乗っている時は以下の計算
; ThrottleUpDown * 乗っているエンティティの速度 * ThrottleUpDownOnEntity -> スロットルの上がりやすさ
; 例：ThrottleUpDownOnEntity = 2.0でマインカートに乗せた場合、最大速度は約1.7なので、
;     1.7 * 2.0 = 3.4、つまり 約1/3の距離で離陸できる

AutoPilotRot = -0.4
; 自動旋回時の角度。大きいほど回転量が大きくなる。
; 0にすると直進する。
; 負の値の場合は左に、正の値の場合は右に回転する。

ConcurrentGunnerMode = true
; 2番席にプレイヤーがいてもガンナーモードになれる。

Regeneration = true
; 2番席以降のモブが自動回復する

ParticlesScale = 0.1
; 砂煙などのエフェクトサイズを変える 0.1
; 大きいほどサイズが大きくなる。

FuelSupplyRange = 25
; 他の機体に燃料を補給するための設定
; この範囲にいる全ての機体に燃料を補給する。上記例では 25m
; 他の機体に補給しても自機の燃料は減らない
; ただし、自機には補給できない

AmmoSupplyRange = 35
; 他の機体に弾を補給するための設定
; この範囲にいる全ての機体に弾を補給する。上記例では 35m
; 他の機体に補給しても自機の弾は減らない
; ただし、自機には補給できない


MaxFuel         = 600
; 燃料搭載可能量
FuelConsumption = 0.5
; 1秒間に消費する燃料
; 飛行可能時間[秒] = 燃料搭載可能量 / 1秒間に消費する燃料
; 1200 sec = 600 / 0.5


Stealth = 0.5
; ステルス性の設定 (0.0～1.0)。
; デフォルト=0.0
; 大きいほどミサイルにロックオンされにくくなる
; ロックにかかる時間が伸び、ロック可能な距離が縮む。

SmoothShading = false
; スムースシェーディングの有効無効の切り替え
; false にすると角が補完されないフラットシェーディングになる
; true  にすると角を滑らかに表示するスムースシェーディングが有効になる
; com.norwood.mcheli.cfg のSmoothShadingをfalseにすると、全ての機体がフラットシェーディングになる

HideEntity = false
; 乗っているプレイヤーを非表示にするかどうかの設定
; true  = 非表示にする
; false = 表示する

EntityWidth  = 0.9
EntityHeight = 0.9
; 乗っているモブの描画サイズ(幅と高さ)を設定する
; 範囲は -100.0 ～ 0.0 ～ 100.0
; 0.5 を設定するとサイズは半分になる

EntityPitch = 45
EntityRoll  = 20
; 乗っているモブの描画時の角度(-360~360)を設定する

CanRide = false
; 機体に乗れなくする設定
; true  = 機体に乗れる(デフォルト)
; false = 機体に乗れない

BoundingBox =  当たり判定の中心X, 当たり判定の中心Y, 当たり判定の中心Z,  判定の幅, 判定の高さ, ダメージ倍率
; 機体の当たり判定を追加する
; 当MODの機銃やミサイルのみ当たる
; ブロックやエンティティにはヒットしない
; コンフィグまたは、ゲーム内のMOD Option からTestModeをONにすると表示できる
; ダメージ倍率は受けるダメージの倍率で記載しない場合 1.0 になる
; ダメージ倍率の例: 0.5 ならダメージ半分、3.0なら3倍のダメージを受ける

Category = W.A
; 機体のカテゴリー設定。
; クリエイティブタブの並び替えのみに使用する。

CanMoveOnGround = false
CanRotOnGround  = false
; falseにすると地上での移動や回転を禁止する
;  CanMoveOnGround は地上での移動を禁止する
;  CanRotOnGround  は地上での回転を禁止する


EnableParachuting = true
; true にするとパラシュート降下を有効にする。
; 降下できるのは3番席以降のモブで、3番席以降のプレイヤーはスペースキーで降下できる
MobDropOption  = 0.0, 0.0, -11.5,  10
; MobDropOption = 降下位置X, 降下位置Y, 降下位置Z, 降下間隔(1/20秒)
; モブを降下させる際の追加オプション


RotorSpeed = 50.0
; ブレードの回転速度。大きいほど早い。
; 負の値にすると逆回転になるが、負の値の使用は推奨しない。


;***********************************************************************************
■ヘリコプター設定ファイル helicopters/abc.txt, models/helicopters/abc.obj, textures/helicopters/abc.png, textures/items/abc.png
;***********************************************************************************

;ヘリコプターを増やすには以下の4ファイルが必要 (全て小文字であること)
; ・helicopters フォルダにヘリコプター設定ファイル
; ・models/helicopters フォルダにヘリコプターのモデル
; ・textures/helicopters フォルダにヘリコプターテクスチャファイル
; ・textures/items フォルダにアイテムのテクスチャファイル

EnableFoldBlade = false
;ブレードの折りたたみ機能の有無(true=有り、false無し)


AddRotor= 6, 60,  0.00,  3.35,  0.00,  0.0, 1.0, 0.0, true
AddRotor= 2, 60,  0.50,  1.90, -6.55,  1.0, 0.0, 0.0
;ローターを追加する (何個でも良い)
; この例では 1つ目がメインローター、2つ目がテールローター
; ブレードを折りたたみできるのは1つ目のローターのブレードのみ
; パラメータは前から順に ブレード数、ブレード間の角度、位置(X,Y,Z)、回転軸(X,Y,Z)、ブレード折りたたみ機能有無(true/false)
; モデルファイル名 機体名_rotor?.obj (命名規則はハッチを参照)

※ AddRotorOld は古いモデル用なので、使用しないで下さい。


AddRepellingHook =  0.60,          2.75,          -14.21,         30
; AddRepellingHook= フックの座標X, フックの座標Y, フックの座標Z,  モブを降ろす間隔


;***********************************************************************************
■戦闘機設定ファイル planes/abc.txt, models/planes/abc.obj, textures/planes/abc.png, textures/items/abc.png
;***********************************************************************************

;戦闘機を増やすには以下の4ファイルが必要 (全て小文字であること)
; ・planes フォルダに戦闘機設定ファイル
; ・models/planes フォルダに戦闘機のモデル
; ・textures/planes フォルダに戦闘機テクスチャファイル
; ・textures/items フォルダにアイテムのテクスチャファイル

AddPartRotor = 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度(-180~180)
; ローターを追加
; スロットルを上げても回転しない。VTOL時に回転する。
; モデルファイル名 機体名_rotor?.obj (命名規則はハッチを参照)
; (オスプレイ以外の機体ではモデルを用意する必要がない)
AddBlade = ブレード描画数, ブレード間の角度, 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z
; AddPartRotor の後に追加しなければならない。
; 各ローターのブレード
; モデルファイル名 機体名_blade?.obj (命名規則はハッチを参照)

AddPartWing = 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180
; 折りたためる主翼を追加
; モデルファイル名 機体名_wing?.obj (命名規則はハッチを参照)
AddPartPylon = 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180
; 折りたためる主翼のパイロンを追加
; モデルファイル名 機体名_wing?_pylon?.obj (命名規則はハッチを参照)
; 必ず AddPartPylon より前に AddPartWing が無ければならない。
; 例
; AddPartWing  =  1.50, 2.50, -4.57,  0, 1,0, 35 この場合のモデル名: 機体名_wing0.obj
; AddPartPylon =  6.69, 2.50, -7.18,  0,-1,0, 35 この場合のモデル名: 機体名_wing0_pylon0.obj
; AddPartPylon =  3.92, 2.50, -6.34,  0,-1,0, 35 この場合のモデル名: 機体名_wing0_pylon1.obj

PivotTurnThrottle = 0.0
; 地上で回るときの移動量
; 0にするとその場で周り、0より大きな値にすると回転時に移動する
; 戦車であれば以下のように設定する。
;  超信地旋回の場合 = 0 
;  信地旋回の場合 = 0より大きな値を設定する

EnableBack = true
;後退可能にする

VariableSweepWing = true
SweepWingSpeed = 1.2
; 可変翼の設定
; AddPartWing で折りたためる主翼を追加した場合のみ有効
; VariableSweepWing = true 空中で主翼を折りたためるようになる
; SweepWingSpeed = 1.2 主翼を折りたたんだ時のスピード

AddPartNozzle = 位置X, 位置Y, 位置Z, 回転軸X, 回転軸Y, 回転軸Z, 回転角度 0～180
; 戦闘機のノズルを追加
; スロットルを上げると煙のエフェクトが出る
; VTOL時に回転する
; モデルファイル名 機体名_nozzle?.obj (命名規則はハッチを参照)
; ノズルから出る煙のエフェクトは ParticlesScale でサイズを変更できる

EnableVtol = true
; VTOL 機能の有無(false=VTOl機能無し, true=VTOl可能)
DefaultVtol = true
; VTOL機能有りの場合の、デフォルトのVTOL状態
; trueにすると地上にいる際に自動でVTOL状態になる
VtolYaw = 0.3
; VTOL 状態での横方向の回転量
VtolPitch = 0.3
; VTOL 状態での仰角の回転量


EnableEjectionSeat = true
;射出座席の有無
; trueにすると、GUIに射出座席が追加される。
; 座席が1つの場合は1つ分、座席が2つの場合は2つ分格納可能。


AddParticleSplash  =  1.0,  0.97,   13.19,      3,     9.0,   1.1,        20, 0.30, -0.03
;AddParticleSplash = 座標X,   Y,    Z,     表示数,  サイズ,  速度,  表示時間, 上昇, 重力
; 「水上で移動時」に水しぶきを発生させる
; EnableSeaSurfaceParticle とは関連がなく、falseにしてもパーティクルは発生する。


EnableSeaSurfaceParticle = true
; 「海上で飛行」した場合に水しぶきを発生させるかどうか
; ParticlesScaleと関連が有り、大きくすると範囲やサイズがおおきくなる。(0.7くらいを推奨)
; AddParticleSplash とは無関係なので注意


;***********************************************************************************
■地上兵器設定ファイル vehicles/abc.txt, models/vehicles/abc.obj, textures/vehicles/abc.png, textures/items/abc.png
;***********************************************************************************

;地上兵器を増やすには以下の4ファイルが必要 (全て小文字であること)
; ・vehicles フォルダに地上兵器設定ファイル
; ・models/vehicles フォルダに地上兵器のモデル
; ・textures/vehicles フォルダに地上兵器テクスチャファイル
; ・textures/items フォルダにアイテムのテクスチャファイル

AddPart = Param1, Param2, Param3, Param4, 位置X, 位置Y, 位置Z
; プレイヤーに合わせて回転するパーツの追加
; Param1 = 1人称視点時に非表示にするかどうか true=表示, false=非表示
; Param2 = プレイヤーに合わせて横方向に回転するか true=回転する, false=回転しない
; Param3 = プレイヤーに合わせて縦方向に回転するか true=回転する, false=回転しない
; Param4 = パーツタイプ、0=通常(機能無し), 1=武器を使用すると回転する, 2=武器を使用すると駐退する
; モデルファイル名 機体名_part?.obj (命名規則はハッチを参照)
AddChildPart = Param1, Param2, Param3, Param4, 位置X, 位置Y, 位置Z
; AddPart に追加するパーツ
; AddPart の後に追加する必要がある。パラメータはAddPartと同じ。
; このパーツは命名規則が特殊。
; モデルファイル名 機体名_part?_#.obj
; AddPart で追加したモデルに更に _# をつける必要がある (#は0から始まる連番)
; 例
; AddPart		=  true, true,  false, 0,   0.00, 0.00, 0.00 → 機体名_part0.obj
; AddChildPart	= false, false, true,  0,  -1.00, 0.00, 2.00 → 機体名_part0_0.obj
; AddChildPart	= false, false, true,  0,   1.00, 0.00, 2.00 → 機体名_part0_1.obj

; RotationPitchMax, RotationPitchMin は古い設定なので使用しないこと。



;***********************************************************************************
■車両設定ファイル tanks/abc.txt, models/tanks/abc.obj, textures/tanks/abc.png, textures/items/abc.png
;***********************************************************************************

;車両を増やすには以下の4ファイルが必要 (全て小文字であること)
; ・tanks フォルダに車両設定ファイル
; ・models/tanks フォルダに車両のモデル
; ・textures/tanks フォルダに車両テクスチャファイル
; ・textures/items フォルダにアイテムのテクスチャファイル


DefaultFreelook = true
; 機体の乗った直後からフリールックにするかどうかの設定
; 主に戦車に使用する

OnGroundPitchFactor = 2.0
OnGroundRollFactor  = 1.3
;地形によって傾く早さ
; この値が大きいほど早く地形に合わせて傾く
; 速度の速い車両は大きめに、遅い車両は小さめにすると良い
; 大きすぎると画面が激しく揺れる
; 小さすぎると地形の傾きに追従しきれずブロックにめり込む

CameraRotationSpeed = 25
; カメラの回転スピード
; 戦車であれば砲塔の回転速度の制限に使用できる

WeightType = Tank
; Tank or Car or Unknown
; 機体の重量タイプ
; Tank : モブにぶつかっても自分にはダメージ無し, 破壊するブロックが多い
; Car  : モブにぶつかると自分もダメージを受ける, 破壊するブロックが少ない
; Tank でも Car でも無い場合の動作は未定義
; 破壊するブロックは com.norwood.mcheli.cfg で設定可能

WeightedCenterZ = 0.0
; 重心Z座標設定
; 機体が地形に合わせて傾くときの重心Z座標
; ※あまりうまく機能しないため、使ってみて違和感があるようなら使わない方が良い

SetWheelPos =  1.75,  -0.24,  4.85, 3.02, 1.44, -1.54, -2.91
;SetWheelPos =  X座標, Y座標,  Z座標1, Z座標2, Z座標3 ...
; 地面との接地位置を指定する。この設置地点に合わせて機体が傾く。
; X座標のマイナス側は必要なし
; ★ Y座標は機体にかかわらず -0.24 を設定することを強く推奨する