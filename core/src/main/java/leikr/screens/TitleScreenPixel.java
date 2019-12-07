/*
 * Copyright 2019 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package leikr.screens;

class TitleScreenPixel {
    public int x = 0;
    public int y = 0;
    public int color = 1;
    public int height = 5;
    public int delay = 0;

    TitleScreenPixel(int xPos, int yPos, int colorIndex, int columnHeight, int delayAmt) {
        x = xPos;
        y = yPos;
        color = colorIndex;
        delay = delayAmt;
        height = columnHeight;
    }

}