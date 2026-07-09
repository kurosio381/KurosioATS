# KurosioATS
 即売会イベント用プラグイン
 
## 使い方・導入
### １．下準備
①Google Cloud Consoleを開く  
　https://console.cloud.google.com/  
②プロジェクトを作成   
　[新しいプロジェクト]  
　プロジェクト名は任意  
③Google Sheets APIを有効化  
　[APIとサービス]→[ライブラリ]  
　「Google Sheets API」を検索し、有効にする。  
④サービスアカウントの作成  
　[APIとサービス]→[認証情報]を開く  
　[認証情報を作成]で[サービスアカウント]を選択   
⑤サービスアカウント名の入力  
　アカウント名は任意。入力後 [作成して続行] をクリック  
⑥認証キー(credentials.json)を作成  
　[IAM と管理]→[サービス アカウント]
　または[API とサービス]→[認証情報]からサービスアカウントを開く  
　さっき作った<アカウント名>をクリック。  
　上部の [Key] タブを開く。  
　[キーを追加] をクリック。  
　[新しいキーを作成] を選択。  
　[JSON] を選んで 「作成」。  
⑦JSONをDL後、PLフォルダ内に配置  
⑧.jarをpluginsフォルダに配置、1回目の読み込み後、自動で以下のファイルが作成されます。  
`config.yml`ー設定用ファイル  
`reception.yml`ー受付用ファイル  
`result.yml`ー抽選後ファイル  
`main.txt`ー`/ats main`で内容を表示させます。  
`rule.txt`ー`/ats rule`で内容を表示させます。  
`massage.txt`ー抽選後に`/ats check`をすることで、`result.yml`に記載されているユーザーのみ内容を表示させます。

### ２．受付
①`/ats status ACCEPTING`で受付状況を変更  
①GoogleFormでMCIDと希望プロットの種類をアンケートで取る。  
②GoogleSpreadsheetにリンクさせる。  
　**`config.yml`を設定**  
③MCIDと希望プロットの列情報を確認  
　例：B列=MCID  
④`/ats sync`でSpreadSheetから`reception.yml`へ内容を同期  

### ３．抽選  
①`/ats lottery`で希望プロットのうち、configのMAX値以内でランダム抽選  
　例：`A→A-3`  
　抽選後は`result.yml`に抽選結果が記録・`/ats check`でプロット場所・`massage.txt`の内容を表示することが可能に。  
②二次抽選を行う場合  
　`reception.yml`に追加で受付情報を同期させても、`/ats lottery`をすることで、追加分も残りプロットのうちで抽選が可能。 
### ４．ワープ設定
設定すると`/ats warp <password>`で指定したブロックの上のみ指定場所へワープすることができます。  
①`/ats warp create <name>`　ワープ地点を設定(スタート位置とTP先位置の2つを設定)  
②`/ats warp set <ワープ設定名> <name1> <name2> <password>`でワープを設定  
　[name1]に立ちパスワードを入力することで[name2]へTPできます。  

## コマンド集 
`/ats help`　コマンド集を表示  
`/ats main`　`main.txt`の内容を表示(開催概要など)  
`/ats rule`　`rule.txt`の内容を表示(ルールなど)  
`/ats check`　自分のプロット位置・抽選後`massage.txt`を表示  
[運営専用]  
`/ats status <BEFORE/ACCEPTING/RESULT>`　受付状況を変更します。  
`/ats sync`　SpreadSheetから`reception.yml`へ同期させます。  
`/ats lottery`　プロット抽選をします。例：希望=A→A-3  
#### ワープ設定
`/ats warp <password>`　指定場所に立ち、TP先へwarpします。  
[運営専用]  
`/ats warp create <name>`　ワープ地点を設定  
`/ats warp set <ワープ設定名> <name1> <name2> <password>`　ワープを設定  
[name1]に立ちパスワードを入力することで[name2]へTPできるように設定

#### .ymlの削除  
`/ats delete reception`　`reception.yml`の内容を削除します。  
`/ats delete result`　`result.yml`の内容を削除します。  

## config.ymlの設定
```prefix: '&f[&b&l即売会&aシステム&f] '
spreadsheet:
 id: '<ID>'
 sheet: "<シート名>"
 credentials: credentials.json
column:
 mcid: 列名
 plot: 列名
max:
 A: <最大プロット数>
 B: <最大プロット数>
 S: <最大プロット数>
lottery:
 exclude:
  - <抽選しないプロット種類>
status: <BEFORE/ACCEPTING/RESULT>
settings:
 clear-before-sync: true
 clear-before-lottery: true
```

