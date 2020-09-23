class DrawDemo extends leikr.Engine {
    enum state {
        START,
        PIXEL,
        LINE,
        RECT,
        CIRCLE,
        SPRITE,
        MAP,
    }
    def prgState = state.START
    def keyTimer = 0
    def btnTimer = 0

    def t = 0
    def ts = 0
    def maxT = 100
    def mx = 0, my = 0, mouseP = []
    def mouseTrails = false

    // clock vars
    def lenS = 20, lenM = 20, lenH = 15
    def clockX = 140, clockY = 30, clockR = 24
    def prevX = 0, prevY = 0

    // cube
    def cubeX = 190, cubeY = 30, cubeS = 10
    def nodes = [[-1, -1, -1], [-1, -1, 1], [-1, 1, -1], [-1, 1, 1],
    [1, -1, -1], [1, -1, 1], [1, 1, -1], [1, 1, 1]]
    def edges = [[0, 1], [1, 3], [3, 2], [2, 0], [4, 5], [5, 7], [7, 6],
    [6, 4], [0, 4], [1, 5], [2, 6], [3, 7]]

    void rotateCuboid(angleX, angleY) {
        def sinX = sin(angleX)
        def cosX = cos(angleX)
        def sinY = sin(angleY)
        def cosY = cos(angleY)

        nodes.each{n ->
            def x = n[0]
            def y = n[1]
            def z = n[2]
            n[0] = x * cosX - z * sinX
            n[2] = z * cosX + x * sinX
            z = n[2]
            n[1] = y * cosY - z * sinY
            n[2] = z * cosY + y * sinY        }
    }

    void scaleCuboid(factor) {
        nodes.each{n ->
            n[0] *= factor
            n[1] *= factor
            n[2] *= factor
        }
    }

    void create(){
        scaleCuboid(cubeS)
        rotateCuboid(3.14159/4, Math.atan(1.41421))
        loadMap("map")
    }

    void update(float delta){
        if ((key("Up") || button("Up")) && keyTimer == 0) {
            keyTimer++
            prgState = prgState.next()
            mouseP = []
        }
        if ((key("Down") || button("Down")) && keyTimer == 0) {
            keyTimer++
            prgState = prgState.previous()
            mouseP = []
        }
        if (!key("Up") && !button("Up") && !key("Down") && !button("Down")) {
            keyTimer = 0
        }

        if (button("A") || key("X") && btnTimer == 0) {
            if (prgState == state.START) {
                prgState = prgState.next()
            }
            btnTimer++
            mouseTrails = !mouseTrails
        }
        if (!key("X") && !button("A")) {
            btnTimer = 0
        }

        // rotateCuboid(3.14159/180, sin(ts/100)/10)
        rotateCuboid(3.14159/180, 0)

        mx = mouseX().intValue()
        my = mouseY().intValue()
    }
    void render(){
    	drawTexture("background-grid-8px.png", 0, 0)

        switch(prgState) {
            case state.START:
                drawString(1, "Press up/down to change test mode", 52, 80)
                drawString(1, "Press X key or A button to toggle mouse trails", 30, 88)

                break
            case state.SPRITE:
                def spriteCount = 0
                nodes.each{n ->
                    sprite(4, cubeX+n[0]-4, cubeY+n[1]-4, 1)
                    ++spriteCount
                }
                // step through all sprites
                def sprid = 0
                drawString(1, "Sprite sheet:", 2, 2)
                for(int y=0; y<8; y++) {
                    for(int x=0; x<8; x++) {
                        drawRect(1, x*16+2, y*16+10, 16, 16)
                        drawString(1, sprid.toString(), x*16+4, y*16+12)
                        sprite(sprid++, x*16+2, y*16+10, 1)
                        ++spriteCount
                    }
                }
                if (mouseTrails) {
                    mouseP.add([mx, my, randInt(32)])
                    mouseP.each{p ->
                        p[0]+=randFloat(2)-1
                        p[1]+=randFloat(2)-0.6
                        sprite(5, p[0]-4, p[1]-4, 1)
                        ++spriteCount
                    }
                    if (mouseP.size() > 130) {
                        mouseP.pop()
                    }
                }
                def msgColor = (spriteCount > 128) ? 8 : 1
                drawString(msgColor, "Sprite draw count: $spriteCount", 135, 100)
                break
            case state.MAP:
                drawMap(0, 0)
                nodes.each{n ->
                    drawMap(cubeX+n[0]-8, cubeY+n[1]-8, 14, 7, 2, 2)
                }
                if (mouseTrails) {
                    mouseP.add([mx, my, randInt(32)])
                    mouseP.each{p ->
                        p[0]+=randFloat(2)-1
                        p[1]+=randFloat(2)-0.6
                        drawMap(p[0]-4, p[1]-4, 21, 2, 1, 1)
                    }
                    if (mouseP.size() > 130) {
                        mouseP.pop()
                    }
                }
                break
            case state.PIXEL:
                // cursor
                drawPixel(8, mx, my)
                drawPixel(8, mx, my-1)
                drawPixel(8, mx, my+1)
                drawPixel(8, mx-1, my)
                drawPixel(8, mx+1, my)

                for(i in 0..32) {
                    drawPixel(i, i+8, 8)
                }
                for(x in 0..115) {
                    def xp = x / 115
                    for(y in 0..4) {
                        def yp = y / 4
                        drawPixel(getColor("${xp*255},${yp*255},0"), x+5, y+16)
                    }
                    drawPixel(x%28+4, x+5, Math.sin((x+t)/120*20) * 20 + 60)
                }

                nodes.each{n ->
                    drawPixel(1, cubeX+n[0], cubeY+n[1])
                }

                if (mouseTrails) {
                    mouseP.add([mx, my, randInt(32)])
                    mouseP.each{p ->
                        p[0]+=randFloat(2)-1
                        p[1]+=randFloat(2)-0.6
                        drawPixel(p[2], p[0], p[1])
                    }
                    if (mouseP.size() > 50) {
                        mouseP.pop()
                    }
                }


                break
            case state.LINE:
                // cursor
                drawLineSegment(8, mx-2, my, mx+3, my)
                drawLineSegment(8, mx, my-2, mx, my+3)

                // horizontal
                drawLineSegment(1, 8, 8, 16, 8)
                drawLineSegment(1, 8, 12, 24, 12)
                drawLineSegment(1, 8, 16, 8, 24)
                drawLineSegment(1, 16, 16, 16, 32)
                
                // isometric W-E
                drawLineSegment(1, 24, 8, 40, 16)
                // isometric E-W
                drawLineSegment(1, 64, 8, 48, 16)

                // 1:1 W-E
                drawLineSegment(1, 24, 16, 40, 32)
                // 1:1 E-W
                drawLineSegment(1, 64, 16, 48, 32)

                // isometric W-E
                drawLineSegment(1, 24, 24, 32, 48)
                // isometric E-W
                drawLineSegment(1, 64, 24, 56, 48)

                // clock
                def now = new Date()
                def ts = now.getSeconds()
                def tm = now.getMinutes()
                def th = now.getHours()
                def th12 = th%12
                def pi2 = 3.14159*2
                def angle = (pi2 * (ts / 60)) - (pi2 / 4)
                def dX = cos(angle) * lenS
                def dY = sin(angle) * lenS
                drawLineSegment("108,118,138", clockX, clockY, clockX + dX, clockY + dY)
                angle = (pi2 * (tm / 60)) - (pi2 / 4)
                dX = cos(angle) * lenM
                dY = sin(angle) * lenM
                drawLineSegment(1, clockX, clockY, clockX + dX, clockY + dY)
                angle = (pi2 * ((th12*5 + (tm/60)*5) / 60)) - (pi2 / 4)
                dX = cos(angle) * lenH
                dY = sin(angle) * lenH
                drawLineSegment("200,220,255", clockX, clockY, clockX + dX, clockY + dY)
                if (tm < 10) tm = "0$tm"
                if (ts < 10) ts = "0$ts"
                drawString(1, "$th:$tm:$ts", clockX - 14, clockY + 28)

                def step = pi2/20
                for(def theta=0; theta<pi2; theta+=step) {
                    def posX = clockX + clockR * cos(theta)
                    def posY = clockY + clockR * sin(theta)
                    drawLineSegment(1, prevX, prevY, posX, posY)
                    prevX = posX
                    prevY = posY
                }

                // cuboid
                edges.each{e ->
                    def p1 = nodes.get(e.get(0))
                    def p2 = nodes.get(e.get(1))
                    drawLineSegment(1, cubeX+p1.get(0), cubeY+p1.get(1), cubeX+p2.get(0), cubeY+p2.get(1))
                }

                if (mouseTrails) {
                    mouseP.add([mx, my])
                    def prevMX = 0, prevMY = 0
                    mouseP.each{p ->
                        if (prevMX == 0) prevMX = p[0]
                        if (prevMY == 0) prevMY = p[1]

                        drawLineSegment(9, prevMX, prevMY, p[0], p[1])
                        prevMX = p[0]
                        prevMY = p[1]
                    }

                    if (mouseP.size() > 20) {
                        mouseP.pop()
                    }
                }
                break
            case state.RECT:
                // cursor
                drawRect(8, mx, my, 4, 4)

                drawRect(1, 0, 0, 8, 8)
                drawRect(1, 8, 0, 4, 4)
                drawRect(1, 16, 0, 2, 2)

                nodes.each{n ->
                    drawRect(1, cubeX+n.get(0)-2, cubeY+n.get(1)-2, 4, 4)
                }

                if (mouseTrails) {
                    mouseP.add([mx, my, 0, randInt(1)])
                    mouseP.each{p ->
                        p[0]+=randInt(1)-1
                        p[1]+=randInt(1)-0.6
                        p[2]+=randFloat(1.6)+0.3
                        if (p[3] == 0) {
                            drawRect(9, p[0], p[1], p[2], p[2])
                        } else {
                            fillRect(9, p[0], p[1], p[2], p[2])
                        }
                    }
                    if (mouseP.size() > 8) {
                        mouseP.pop()
                    }
                }

                break
            case state.CIRCLE:
                // cursor
                drawCircle(8, mx, my, 4)

                def s4 = ts % 6
                drawCircle(1, 28, 4, 4)
                drawCircle(1, 36, 4, 2)

                fillCircle(1, 44, 4, 4)
                fillCircle(1, 52, 4, 2)

                drawCircle(1, 8, 16, 8)
                drawCircle(1, 24, 16, 6)

                drawCircle(1, 48, 48, s4+1)
                drawCircle(1, 64, 48, s4+1)

                nodes.each{n ->
                    fillCircle(1, cubeX+n[0], cubeY+n[1], 2)
                }

                if (mouseTrails) {
                    mouseP.add([mx, my, 0, randInt(1)])
                    mouseP.each{p ->
                        p[0]+=randInt(4)-2
                        p[1]+=randInt(4)-1.5
                        p[2]+=randFloat(1.6)
                        if (p[3] == 0) {
                            drawCircle(9, p[0], p[1], p[2])
                        } else {
                            fillCircle(9, p[0], p[1], p[2])
                        }
                    }
                    if (mouseP.size() > 15) {
                        mouseP.pop()
                    }
                }
                break
            default:
                drawString(1, "Unknown mode: ${prgState}", 70, 80)
        }

        drawString(1, "Test mode: ${prgState}", 4, 152)
        drawString(1, "Cursor: ${mx}, ${my}", 80, 152)
        drawString(1, "Grid size: 8px", 182, 152)

        t++
        ts = Math.floor(t/48)
    }
}

