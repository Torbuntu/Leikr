from leikr import Engine

class PythonGame(Engine):

    def __init__(self):
        self.x = 10
        print("init from python")
    #

    def create(self):
        print("hey")
        self.x = 10
    #

    def update(self):
        print("hey2")
    #

    def render(self):
        if(key("Right")):
            self.x = self.x + 5
            print("right")

        if(key("Left")):
            self.x = self.x - 5
            print("left")
        self.square(self.x, 100, 25, 25, "red")
    #

#

game = PythonGame()
