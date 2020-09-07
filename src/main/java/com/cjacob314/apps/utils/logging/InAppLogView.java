/* Copyright (C) Jacob Cohen - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
package com.cjacob314.apps.utils.logging;

import com.cjacob314.apps.persistence.IniManager;
import com.cjacob314.apps.utils.ColorUtils;
import com.jfoenix.controls.JFXTextArea;

/**
 *
 * @author Jacob Cohen <jcohen30@uic.edu> or <jacob@jacobcohen.info>
 */
public class InAppLogView {
    private static final JFXTextArea textArea;

    static{
        textArea = new JFXTextArea();
        textArea.setPrefSize(200d, 75d);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setStyle(String.format("-fx-background-color:#e0e0e0; -fx-font-family: Consolas; -fx-text-fill: %s; -fx-font-size: 14px; -jfx-focus-color: %s; -jfx-unfocus-color: #e0e0e0;",
                ColorUtils.darkenColorStr(IniManager.windowThemeColor, 0.3d), IniManager.windowThemeColor));
    }

    public static void write(String toWrite){
        textArea.appendText(toWrite);
    }

    public static JFXTextArea getMain(){
        return textArea;
    }

}
