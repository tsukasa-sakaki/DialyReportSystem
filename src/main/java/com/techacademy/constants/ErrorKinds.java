package com.techacademy.constants;

// エラーメッセージ定義
public enum ErrorKinds {

    // エラー内容
    // 空白チェックエラー
    BLANK_ERROR,
    /*
    NAME_BLANK_ERROR, //追加
    TITLE_BLANK_ERROR, //追加
    CONTENT_BLANK_ERROR, //追加
    DATECHECK_BLANK_ERROR, //追加
    */
    // 半角英数字チェックエラー
    HALFSIZE_ERROR,
    // 桁数(8桁~16桁以外)チェックエラー
    RANGECHECK_ERROR,
    /*
    NAME_RANGECHECK_ERROR, //追加
    TITLE_RANGECHECK_ERROR, //追加
    CONTENT_RANGECHECK_ERROR, //追加
    */
    // 重複チェックエラー(例外あり)
    DUPLICATE_EXCEPTION_ERROR,
    // 重複チェックエラー(例外なし)
    DUPLICATE_ERROR,
    // ログイン中削除チェックエラー
    LOGINCHECK_ERROR,
    // 日付チェックエラー
    DATECHECK_ERROR,
    // チェックOK
    CHECK_OK,
    // 正常終了
    SUCCESS;

}
