from leikr import Engine

class Python(Engine):

    def __init__(self):
        super(Python, self).__init__()
        self.x = 10
    #

    def create(self):
        print("hey")
    #

    def update(self):
        print("hey2")
        #Do nothing yet
    #

    def render(self):
        if(self.key("Right")):
            self.x = self.x + 5

        if(self.key("Left")):
            self.x = self.x - 5

        square(self.x, 100, 25, 25)
    #


#

game = Python()
