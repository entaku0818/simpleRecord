# シンプル録音アプリ 残りの作業計画

## コミット1: 録音機能の拡張 ✅
- ✅ 録音の一時停止と再開機能
- ✅ 録音設定を保存するための設定クラスを作成
- ✅ 録音品質を選択するための基本UI

## コミット2: 再生機能の拡張 ✅
- ✅ 再生中の音声波形表示
  - 音声データからリアルタイムで波形を生成
  - 波形表示コンポーネントの実装
  - 再生位置に合わせた波形のハイライト
- ✅ 再生速度の調整機能
  - 0.5x, 1.0x, 1.5x, 2.0xの速度オプション
  - MediaPlayerのPlaybackParamsを使用した実装
- ✅ 連続再生モード
  - 複数の録音を連続して再生する機能
  - 再生キューの管理
  - 次/前の録音へのスキップボタン

## コミット3: プレイリスト機能の基本実装
- プレイリストのデータモデルとDAO
  - Room Entityとしてのプレイリストクラスの作成
  - プレイリストと録音のリレーション実装
  - PlaylistDaoインターフェースの実装
- プレイリスト画面のComposable
  - プレイリスト一覧画面
  - プレイリスト詳細画面
  - プレイリスト作成/編集ダイアログ
- プレイリスト操作のViewModel
  - プレイリストのCRUD操作
  - 録音をプレイリストに追加/削除

## コミット4: プレイリスト機能の拡張
- プレイリスト内の音声メモの並べ替え
  - Jetpack Composeでのドラッグ&ドロップ実装
  - 並び順の永続化
- プレイリスト単位での再生機能
  - プレイリスト再生モード
  - ExoPlayerを使用した連続再生の実装
  - リピート/シャッフル再生オプション

## コミット5: 設定画面の拡張
- 設定画面のComposable実装
  - PreferenceDataStoreを使用した設定管理
  - 設定変更の即時反映
- エラーログの表示機能
  - Timberを使用したログ収集
  - ログ表示画面の実装
  - ログのシェア機能
- マイク音量の調整機能
  - AudioRecordを使用した音量調整
  - 音量レベルのプレビュー

## コミット6: チュートリアル機能
- 初回起動時のチュートリアル表示
  - DataStoreでのフラグ管理
  - オーバーレイ表示の実装
- 基本操作の説明画面
  - ViewPagerでの3ステップ説明
  - スワイプによるページ切り替え
  - スキップボタンの実装
- 多言語対応の基盤実装
  - strings.xmlリソース構造の設計
  - 基本的な翻訳リソースの追加

## コミット7: 多言語対応の拡張
- 12言語のリソース追加
  - 英語 (en)
  - ドイツ語 (de)
  - スペイン語 (es)
  - フランス語 (fr)
  - イタリア語 (it)
  - 日本語 (ja)
  - ポルトガル語 (pt-PT)
  - ロシア語 (ru)
  - トルコ語 (tr)
  - ベトナム語 (vi)
  - 簡体字中国語 (zh-Hans)
  - 繁体字中国語 (zh-Hant)
- 言語切替機能
  - 言語選択画面
  - LocaleManagerでの言語設定

## コミット8: クラウド同期機能
- Firebaseとの連携
  - Firebase Authentication
  - Cloud Firestore
  - Firebase Storage
- データ同期の実装
  - WorkManagerを使用した同期処理
  - Google Driveバックアップ
  - オフラインキャッシュ対応

## コミット9: 課金機能（プレミアムサービス）
- Google Play Billing実装
  - Billing Library 5.0
  - サブスクリプション管理
  - 購入状態の検証
- プレミアム機能の実装
  - 広告非表示
  - 無制限のプレイリスト作成
  - 高度な音声編集機能
  - 1ヶ月無料トライアル
