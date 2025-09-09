
2016/04/17

;***********************************************************************************
;■武器設定ファイル weapons/***.txt, sound/***_snd.ogg
;***********************************************************************************

; ★ 重要 ★
; 武器設定ファイルはマインクラフトを起動したままでも再読み込みできる。
; 機体に乗る → Rキーで補給画面 → MOD Option → Development → Reload All Weapons
; これは携帯兵器も含めて全兵器が再読み込みされる。

;武器を増やすには以下の2ファイルが必要 (全て小文字であること)
; ・weaponsフォルダに武器設定ファイル(txt)を増やす
; ・soundフォルダに武器設定ファイルと同じ名前+_sndの使用音(ogg)を増やす
; 例えば abc という武器であれば weapons/abc.txt と sound/abc_snd.ogg が必要
; ※0.9.4からは音声ファイルは必須ではなくなった。

;数値パラメータには上下限が存在するものがある。

DisplayName = M134 Minigun
;表示される名前 ※全角を使用しないこと、半角英数字と記号のみ

Type = MachineGun1
;武器タイプ 以下から1つ設定できる
;	MachineGun1	向き固定のマシンガン ... M134など
;	MachineGun2	プレイヤーの視点に向きを合わせるマシンガン ... M230
;	Torpedo		水中に落とすと水中で目標に向かう魚雷 ... Mk46
;	CAS			目標地点に近接航空支援を行う ... A-10
;	Rocket		向き固定の無誘導ロケット ... Hydra70 や SNEB68mm
;	ASMissile	地上の目標地点に飛んで行くミサイル ... AGM119
;	AAMissile	空中にいるモブを追跡するミサイル ... AIM92
;	TVMissile	発射後プレイヤーが向きを操作できるMissile ... AGM114[TV]
;	ATMissile	地上にいるモブを追跡するミサイル ... AGM114
;	Bomb		真下に投下する爆弾 ... CBU-100
;	MkRocket	着弾地点に砲撃させるロケット ... Hydra 70mm M264RP
;	Dummy		使用出来ない武器。武器欄に文字を表示するのに使用。
;	Smoke		飛行機雲を発生させる ... Smoke white
;	Dispenser	着弾地点にアイテムを使用する ... Water Dispenser(Till)
;	TargetingPod モブやプレイヤーのスポット、ブロックのマークを行う ... targeting_pod_block

Power = 8
;ダメージ

DamageFactor = tank, 2.0
;ダメージ倍率
; 1番目のパラメータには以下のいずれかを設定する
;  player : プレイヤー
;  heli または helicopter : ヘリコプター
;  plane : 固定翼機
;  tank : 戦車または自動車
;  vehicle : 地上兵器
; 2番目のパラメータにはダメージの倍率を設定する[0~]。 3.4でPowerが10なら34のダメージになる
; この設定は複数行書いた場合、それぞれ反映される

Acceleration = 4.0
;弾の速度 (一部を除き最大4.0)
; MachineGun1, MachineGun2, Rocket のみ最大100.0まで可能

AccelerationInWater = 4.0
;魚雷の水中での速度 (最大4.0)

VelocityInWater = 0.5
;水中での加速度
; 水中ではTick毎にこの値が速度に乗算される。

Explosion = 0
;着弾時の爆発の威力(0=爆発なし、1=ガストの弾の威力、2～)
ExplosionInWater = 0
;水中での着弾時の爆発の威力
ExplosionBlock = 0
;着弾時の爆発のブロック破壊力。0だとブロックを破壊しない
ExplosionAltitude = 10
;爆発する地上からの高さ
;10にすると地上から10m以内に入ると爆発する

DelayFuse = 30
;遅延信管：着弾から弾が消滅するまでのカウント
; Explosion/ExplosionInWater が0でない場合は、消滅時に爆発する

Bound = 0.4
;着弾時の跳ね返りの強さ
; Boundを使用する場合、DelayFuse も設定しないと着弾直後に爆発するため意味が無い

TimeFuse = 30
;時限信管：発射から弾が消滅するまでのカウント
; Explosion/ExplosionInWater が0でない場合は、消滅時に爆発する

Flaming = false
;着弾時に炎を撒くかどうか(false=無効、true=有効)
; ※ Explosion > 0 の時のみ有効

Sight = MoveSight or None or MissileSight
;画面に表示される照準
;	MoveSight		機体の向きにあわせて移動する照準
;	MissileSight	モブをロックするタイプの照準(AAMissile/ATMissileでは必須)

Zoom = 4.2, 9.2
; 携帯兵器のみの設定
; スコープを除いた時の倍率。, で区切って複数設定すると、Zキーで切り替えできるようになる

Group = MainGun
; 武器のグループを設定する
; 同じ武器のグループはどれか1つでも使用すると全てリロードされる
; 具体的には戦車の主砲の武器を弾種ごと以下のように分け、
; rehinmetall_apfsds.txt, rehinmetall_he.txt, canistershell.txt の3つに Group = MainGun を設定すると
; 1つでも使用すると他の2つもリロードされる。弾は減らない
;  1番目: rehinmetall_apfsds
;  2番目: rehinmetall_he
;  3番目: canistershell
; これは1番目の武器を使用後、すぐに2番目に切り替えて撃つような動作を防ぐための設定


Delay = 5
;次の使用までの待ち時間(約1/20秒単位)、小さいほど早い

ReloadTime = 80
;リロード完了までの待ち時間(1/20秒単位)、小さいほど早い
; ※リロード時間を0にする際は装弾数を0以外に設定すること

Round = 100
;装弾数 使用しない時は 0 を設定するか、このパラメータ自体を記載しない

SoundVolume = 3
;武器使用時の音量
; マイクラの仕様で 1.0 以上に設定すると最大音量となる
; (音量を下げる場合は 1,0 未満にする)
; 1.0 を超えると遠くからでも音声が聞こえる

SoundPitch = 1.0
;音の高さ (0.0 ~ 1.0)

SoundPitchRandom = 0.1
;音の高さがランダムで変わる範囲 (0.0 ~ 1.0)
; 以下の例では、最終的に再生される音声のピッチは 0.6～0.8 になる。
; SoundPitch = 0.8
; SoundPitchRandom = 0.2

SoundDelay = 1
;音声を連発した際に、次の音を鳴らすまでの待ち時間
;M134などはこの設定がないと音声を連発し過ぎで他の音が消える

Sound = rocket_snd
;音声ファイルの指定。拡張子は不要
;この設定で音声ファイルを指定しない場合、武器名_snd.ogg が使われる。

LockTime = 20
;ロックオンするタイプのミサイルのロックまでの時間。大きいほどロックまで時間がかかる。

RidableOnly = true
; プレイヤーを機体に乗っているときのみロック可能にする設定

ProximityFuseDist = 1.0
;ロックオンするタイプのミサイルの近接信管の動作距離
; 1 を指定すると 1m 以内に入ったら爆発

RigidityTime = 0
; ミサイルを撃ってから追尾を開始するまでのカウント
; 記載しない場合、デフォルト値として  7 が設定される

Accuracy = 1
; 無誘導の銃弾やロケットの誤差。大きいほど誤差が大きい。

Bomblet = 25
;使用後、子弾が展開する数。クラスター爆弾などに使用。
BombletSTime = 5
;子弾が展開するまでの時間
BombletDiff = 0.7
;子弾の拡散率

ModeNum = 2
; Xキーで切り替えられる武器のモード数
; 以下の武器タイプでのみ設定可能
; Type = MachineGun2 → HE弾に切り替え(爆発する弾)。Explosion が0の場合無効。
; Type = TVMissile   → 通常誘導弾に切り替え(ミサイル視点でない誘導弾)
; Type = ATMissile   → TAモード(TopAttack)に切り替え(敵の前で上に上がったあとに急降下する)
; Type = Rocket      → HEIAP弾に切り替え(空中で複数の子弾を撒く)
; 現在 1か2のみ設定可能。
; 省略時は1

Piercing = 2
; ブロックの貫通回数

HeatCount = 20
;銃身熱量式の武器の、1回の使用で上がる熱量
MaxHeatCount = 150
;熱量上限

FAE = true
;燃料気化爆弾のON/OFF
;true で燃料気化爆弾になる
;燃料気化爆弾はブロックを破壊しない

ModelBullet = bullet
ModelBomblet = cbc
;弾丸のモデルファイル指定
; 上記の場合 models/bullets/bullet.obj と textures/bullets/bullet.png が使用される。
; クラスター爆弾の子弾には models/bullets/cbc.obj と textures/bullets/cbc.png が使用される。

Destruct = true
;使用すると機体が自爆する
; Type = Bomb の場合のみ使用可能。
; 機体が UAVのヘリコプター の場合のみ効果がある。

Gravity = -0.04
GravityInWater = 0.0
;弾頭の落下速度
; 絶対値が大きいほど早く下に落ちる
; GravityInWater は水中での落下速度

GuidedTorpedo = true
;魚雷の誘導/無誘導を切り替える設定
; true  にすると誘導魚雷になり、指定したブロックに向かう
; false にすると無誘導魚雷になり、落下した位置からまっすぐに進む

TrajectoryParticle = flame
; 特定武器の使用時の「軌跡」のエフェクトを指定する（ミサイルが追尾しているときのパーティクルなど）
; none          ...エフェクト無し
; explode       ...煙のエフェクト
; flame         ...炎のエフェクト
; hugeexplosion ...煙のエフェクト
; largeexplode  ...煙のエフェクト
; largesmoke    ...煙のエフェクト
; smoke         ...煙のエフェクト
;
; 詳しい方向けへの説明: spawnParticle に設定する文字列なので、他の文字列も指定可能。
; Particle は1.0.0から廃止。代わりに AddMuzzleFlash または AddMuzzleFlashSmoke を使用すること。

TrajectoryParticleStartTick = 10
; TrajectoryParticle のエフェクトが出始めるまでのカウント


DisableSmoke = true
; 特定の武器の煙のエフェクトを無効化する
; (射撃時のエフェクトではなく、移動時に出る煙のエフェクト)


AddMuzzleFlash  =  0.5,             0.20,   1,        150,254,219,184
;AddMuzzleFlash = 発射元からの距離, サイズ, 表示時間,  A,  R,  G,  B
; ★注意!：武器の使用間隔が5辺りだと正常に表示できないことがあるため、使用しないこと。

AddMuzzleFlashSmoke  =  2.2,             1,       5.0,    2.0,  15,      180,250,245,240
;AddMuzzleFlashSmoke = 発射元からの距離, 表示数, サイズ, 範囲, 表示時間,  A,  R,  G,  B
; ★注意!：武器の使用間隔が5辺りだと正常に表示できないことがあるため、使用しないこと。


SetCartridge = cartridge, 0.0, 0, 0, 2.00, -0.04, 0.40
; 武器使用時に空薬莢を落とす設定
; SetCartridge = model_name, Acceleration, Yaw, Pitch, ModelScale, Gravity, Bound
; model_name   : モデル名 全て小文字、半角
; Acceleration : 飛ばす強さ 0だと真下に落ちる
; Yaw          : 武器からみて横方向に飛んで行く角度(Yaw)   正の値:左、負の値:右
; Pitch        : 武器からみて縦方向に飛んで行く角度(Pitch) 正の値:下、負の値:上
; ModelScale   : 表示倍率
; Gravity      : 重力の強さ
; Bound        : ブロックにぶつかった時の跳ね返りの強さ


MaxAmmo = 40
; 機体が保持できる最大弾数
SuppliedNum = 10
; 1回の弾薬補給で追加される弾数
Item =  3, iron_ingot
Item =  4, gunpowder
Item =  2, redstone
; 補給に必要なアイテムとスタック数 (鉄インゴット、火薬、レッドストーンのみ指定可能)

; MaxAmmo = 40
; SuppliedNum = 10
; Item =  3, iron_ingot
; Item =  4, gunpowder
; Item =  2, redstone
; この例では、1回のリロードで 鉄インゴット3個、火薬4個、レッドストーン2つ消費して
; 10発補給できる。40発まで補給できるので最大まで補給すると
; 鉄インゴット12個、火薬16個、レッドストーン8つ を消費する


BulletColor        = 255, 255, 255, 255
BulletColorInWater = 255,  25,  25,  75
; 弾の色設定(0～255)。前から順に Alpha, Red, Green, blue
; BulletColorInWater は水中での色を指定する。

SmokeColor  = 230, 200, 20, 80
; スモークの色設定(0～255)。前から順に Alpha, Red, Green, blue
SmokeSize   = 2.0
; スモークのサイズ
SmokeMaxAge = 500
; スモークの表示時間


DisplayMortarDistance = true
; 着弾距離の表示

FixCameraPitch = true
; カメラの垂直方向を0に固定する

CameraRotationSpeedPitch = 0.3
; カメラの回転速度の倍率(小さくするとより細かく着弾地点が調整ができる)



DispenseItem = flint_and_steel
; 着弾時に指定したアイテムを使用する
; 効果のあるアイテムと無いアイテムが存在するため
; 使ってみないとわからない。
; ↑の設定の場合着弾地点に火打ち石を使う
; 例外は 水入りバケツ(water_bucket) で水をセットするのではなく
; 着弾地点付近の火＆マグマを消火する効果になる。

DispenseRange = 4
; DispenseItemで指定したアイテムの使用範囲(単位：ブロック)


Recoil = 1.1
; 武器を使用した時の機体の揺れの強さ


RecoilBufCount = 40, 5
; RecoilBufCount = 駐退カウント, 後退中のカウント倍率
;  駐退カウント を大きくすると、駐退全体の時間が延びる
;  後退中のカウント倍率 を大きくすると、後退のみ時間が早くなる


Target = monsters/others
; スポットするエンティティの種類、または、ブロックをマークするかどうかを指定する
; 設定は以下から複数指定できる。複数指定するときは / で区切る
; 例外は block で、blockを指定すると他の指定が無効化される
;
;  planes		ヘリMODの固定翼機
;  helicopters	ヘリMODのヘリコプター
;  vehicles		ヘリMODの地上兵器
;  players		他のプレイヤー
;  monsters		モンスター
;  others		その他のモブ
;  block		ブロックをマークする機能になる。スポット機能ではなくなる。

Length = 100
; スポットする距離を指定する
; 単位はブロックで、15であれば直線よりで15ブロックの距離

Radius = 45
; スポットする可能な範囲の広さを角度で指定する
; 45 であれば半径45度となる

MarkTime = 10
; スポットの表示時間を秒単位で指定する
; 10であれば10秒
