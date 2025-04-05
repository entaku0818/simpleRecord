# コミット1: 録音機能の拡張

## 追加機能
- 録音の一時停止と再開機能
- 録音設定の管理システム
- 録音品質の設定画面（ファイル形式、サンプリングレート、ビットレート、チャンネル数）

## 変更内容
1. 録音状態に`PAUSED`を追加し、一時停止と再開のロジックを実装
2. 設定を管理する`SettingsManager`クラスを作成
3. 録音設定画面`RecordingSettingsScreen`を実装
4. 録音画面に一時停止/再開ボタンと設定画面へのナビゲーションを追加
5. `RecordViewModel`を拡張して設定を適用するように変更

## 変更ファイル
- 新規: `app/src/main/java/com/entaku/simpleRecord/settings/SettingsManager.kt`
- 新規: `app/src/main/java/com/entaku/simpleRecord/settings/RecordingSettingsScreen.kt`
- 更新: `app/src/main/java/com/entaku/simpleRecord/record/RecordViewModel.kt`
- 更新: `app/src/main/java/com/entaku/simpleRecord/record/RecordViewModelFactory.kt`
- 更新: `app/src/main/java/com/entaku/simpleRecord/record/RecordScreen.kt`
- 更新: `app/src/main/java/com/entaku/simpleRecord/MainActivity.kt`

## 詳細
- 録音中に一時停止ボタンを押すと録音を一時停止できるようになりました
- 一時停止中に再開ボタンを押すと録音を再開できるようになりました
- 設定画面から録音のファイル形式、サンプリングレート、ビットレート、チャンネル数を選択できるようになりました
- 設定はSharedPreferencesに保存され、アプリを再起動しても維持されます
