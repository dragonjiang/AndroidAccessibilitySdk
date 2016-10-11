package com.demo.dragonjiang.accessilibility_sdk.shell_support;

import android.text.TextUtils;
import android.util.Log;

import com.demo.dragonjiang.accessilibility_sdk.core.command.ICommand;
import com.demo.dragonjiang.accessilibility_sdk.core.command.execption.NoCmdException;
import com.demo.dragonjiang.accessilibility_sdk.utils.ThreadUtil;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author DragonJiang
 * @Date 2016/7/30
 * @Time 22:57
 * @description
 */
public class ShellCmdUtil {

    private static final String TAG = "ShellCmdUtil";
    private static final String COMMAND_RESULT_FORMAT = "resultCode:%s,successMsg:%s,errorMsg:%s\n";

    public static void execShellCommand(final String strCommand, final IShellCmdCallback listener) {
        if (TextUtils.isEmpty(strCommand)) {
            //如果输入为空，则返回输入为空的错误提示
            listener.OnInputError("command can not be Empty");
            return;
        }

        ThreadUtil.executeSingle(new Runnable() {
            @Override
            public void run() {
                //执行adb命令
                CommandResult result = ShellUtils.execCommand(strCommand, true, true);
                //将结果格式化转换
                final String strResult = String.format(COMMAND_RESULT_FORMAT, result.result, result.successMsg,
                        result.errorMsg);
                listener.OnComplete(strResult);
            }
        });
    }

    public static Observable<Integer> execShellCmdObservable(final String strCommand) {
        if (TextUtils.isEmpty(strCommand)) {
            return Observable.error(new NoCmdException());
        }

        return Observable.just(ShellUtils.execCommand(strCommand, true, true))
                .flatMap(new Func1<CommandResult, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(CommandResult result) {
                        //将结果格式化转换
                        final String strResult = String.format(COMMAND_RESULT_FORMAT, result.result, result
                                .successMsg, result.errorMsg);
                        Log.i(TAG, strResult);

                        int r = result.result >= 0 ? ICommand.EXEC_SUCCESS : ICommand.EXEC_FAILURE;
                        return Observable.just(r);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /************************************************************/
    /************************************************************/
    /***Usage: input [<source>] <command> [<arg>...]*************/
    /***source:trackball  joystick  touchnavigation  mouse
     * keyboard  gamepad  touchpad  dpad  stylus  touchscreen****/
    /************************************************************/

    /**
     * input text <string>
     *
     * @param text
     * @return
     */
    public static String buildTextCmd(final String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }

        return "input text " + text;
    }


    /**
     * input keyevent <key code number or name>
     *
     * @param keyCode
     * @return
     */
    public static String buildKeyEventCmd(final int keyCode) {
        if (keyCode < 0 || keyCode > 91) {
            return "";
        }

        return "input keyevent " + keyCode;
    }


    /**
     * input tap <x> <y>
     *
     * @param x
     * @param y
     * @return
     */
    public static String buildTapCmd(final int x, final int y) {
        return "input tap " + x + " " + y;
    }

    /**
     * input swipe <x1> <y1> <x2> <y2>
     *
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @return
     */
    public static String buildSwipeCmd(final int sx, final int sy, final int ex, final int ey) {
        return "input tap " + sx + " " + sy + " " + ex + " " + ey;
    }

    /**
     * press (Default: trackball)
     *
     * @return
     */
    public static String buildPressCmd() {
        return "input press";
    }


    /**
     * roll <dx> <dy> (Default: trackball)
     *
     * @param dx
     * @param dy
     * @return
     */
    public static String buildRollCmd(final int dx, final int dy) {
        return "input roll " + dx + " " + dy;
    }

    public interface KEY_CODE {
        int KEYCODE_UNKNOWN = 0;
        int KEYCODE_SOFT_LEFT = 1;
        int KEYCODE_SOFT_RIGHT = 2;
        int KEYCODE_HOME = 3;
        int KEYCODE_BACK = 4;
        int KEYCODE_CALL = 5;
        int KEYCODE_ENDCALL = 6;
        int KEYCODE_0 = 7;
        int KEYCODE_1 = 8;
        int KEYCODE_2 = 9;
        int KEYCODE_3 = 10;
        int KEYCODE_4 = 11;
        int KEYCODE_5 = 12;
        int KEYCODE_6 = 13;
        int KEYCODE_7 = 14;
        int KEYCODE_8 = 15;
        int KEYCODE_9 = 16;
        int KEYCODE_STAR = 17;
        int KEYCODE_POUND = 18;
        int KEYCODE_DPAD_UP = 19;
        int KEYCODE_DPAD_DOWN = 20;
        int KEYCODE_DPAD_LEFT = 21;
        int KEYCODE_DPAD_RIGHT = 22;
        int KEYCODE_DPAD_CENTER = 23;
        int KEYCODE_VOLUME_UP = 24;
        int KEYCODE_VOLUME_DOWN = 25;
        int KEYCODE_POWER = 26;
        int KEYCODE_CAMERA = 27;
        int KEYCODE_CLEAR = 28;
        int KEYCODE_A = 29;
        int KEYCODE_B = 30;
        int KEYCODE_C = 31;
        int KEYCODE_D = 32;
        int KEYCODE_E = 33;
        int KEYCODE_F = 34;
        int KEYCODE_G = 35;
        int KEYCODE_H = 36;
        int KEYCODE_I = 37;
        int KEYCODE_J = 38;
        int KEYCODE_K = 39;
        int KEYCODE_L = 40;
        int KEYCODE_M = 41;
        int KEYCODE_N = 42;
        int KEYCODE_O = 43;
        int KEYCODE_P = 44;
        int KEYCODE_Q = 45;
        int KEYCODE_R = 46;
        int KEYCODE_S = 47;
        int KEYCODE_T = 48;
        int KEYCODE_U = 49;
        int KEYCODE_V = 50;
        int KEYCODE_W = 51;
        int KEYCODE_X = 52;
        int KEYCODE_Y = 53;
        int KEYCODE_Z = 54;
        int KEYCODE_COMMA = 55;
        int KEYCODE_PERIOD = 56;
        int KEYCODE_ALT_LEFT = 57;
        int KEYCODE_ALT_RIGHT = 58;
        int KEYCODE_SHIFT_LEFT = 59;
        int KEYCODE_SHIFT_RIGHT = 60;
        int KEYCODE_TAB = 61;
        int KEYCODE_SPACE = 62;
        int KEYCODE_SYM = 63;
        int KEYCODE_EXPLORER = 64;
        int KEYCODE_ENVELOPE = 65;
        int KEYCODE_ENTER = 66;
        int KEYCODE_DEL = 67;
        int KEYCODE_GRAVE = 68;
        int KEYCODE_MINUS = 69;
        int KEYCODE_EQUALS = 70;
        int KEYCODE_LEFT_BRACKET = 71;
        int KEYCODE_RIGHT_BRACKET = 72;
        int KEYCODE_BACKSLASH = 73;
        int KEYCODE_SEMICOLON = 74;
        int KEYCODE_APOSTROPHE = 75;
        int KEYCODE_SLASH = 76;
        int KEYCODE_AT = 77;
        int KEYCODE_NUM = 78;
        int KEYCODE_HEADSETHOOK = 79;
        int KEYCODE_FOCUS = 80;//*Camera*focus
        int KEYCODE_PLUS = 81;
        int KEYCODE_MENU = 82;
        int KEYCODE_NOTIFICATION = 83;
        int KEYCODE_SEARCH = 84;
        int KEYCODE_MEDIA_PLAY_PAUSE = 85;
        int KEYCODE_MEDIA_STOP = 86;
        int KEYCODE_MEDIA_NEXT = 87;
        int KEYCODE_MEDIA_PREVIOUS = 88;
        int KEYCODE_MEDIA_REWIND = 89;
        int KEYCODE_MEDIA_FAST_FORWARD = 90;
        int KEYCODE_MUTE = 91;
    }
}
