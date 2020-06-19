/**
 * License
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Kay Stenschke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.kstenschke.finderinfo.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.kstenschke.finderinfo.resources.Icons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class FinderInfoAction extends AnAction {

    /**
     * Hide menu item if no file selected
     *
     * @param   event   Action system event
     */
    public void update(AnActionEvent event) {
        boolean visible = false;

        VirtualFile file = this.getVirtualFile(event);
        if( file != null ) {
            visible = true;
        }

        Presentation presentation = event.getPresentation();
        presentation.setVisible(visible);

        if( visible ) {
            presentation.setIcon(Icons.ICON_ACTION);
        }
    }

    private VirtualFile getVirtualFile(AnActionEvent event) {
        return PlatformDataKeys.VIRTUAL_FILE.getData(event.getDataContext());
    }

    /**
     * Reveal info of selected file
     *
     * @param   event   AnActionEvent
     */
    public void actionPerformed(AnActionEvent event) {
        VirtualFile file = this.getVirtualFile(event);
        if( file != null ) {
            String command[] = {
                    "osascript", "-e",
                    "tell application \"Finder\" to open information window of (POSIX file \"" + file.getCanonicalPath() + "\" as alias)"
            };
            try {
                Process process = Runtime.getRuntime().exec( command );
                printErrorStream(process);
            } catch (IOException e) {
                System.out.println( "IOException: " + e.getMessage() );
            }

            String command2[] = { "osascript", "-e", "tell application \"Finder\" to activate" };
            try {
                Process process = Runtime.getRuntime().exec( command2 );
                printErrorStream(process);
            } catch (IOException e) {
                System.out.println( "IOException: " + e.getMessage() );
            }
        }
    }

    private void printErrorStream(Process process) throws IOException {
        String lsString;
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));
        while ((lsString = bufferedReader.readLine()) != null) {
            System.out.println(lsString);
        }
    }

}