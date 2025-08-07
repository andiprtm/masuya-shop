/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author andid
 */
public class OperationResult {
    private final boolean success;
    private final String message;

    private OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /** Pembuatan instance sukses */
    public static OperationResult ok(String message) {
        return new OperationResult(true, message);
    }

    /** Pembuatan instance gagal */
    public static OperationResult fail(String message) {
        return new OperationResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
