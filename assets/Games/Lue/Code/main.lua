local game = require("game")

local Lue = {}
setmetatable(Lue, {__index=game})

function Lue:init()

end

function Lue:update()
end

function Lue:render()
	square(10, 10, 25, 25)
end

game = Lue()

