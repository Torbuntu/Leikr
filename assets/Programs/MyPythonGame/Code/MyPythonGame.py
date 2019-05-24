from leikr import Engine

class MyPythonGame(Engine):
	
	def __init__(self):
		super(MyPythonGame, self).__init__()
		self.y = 0

		self.sy = 0
	#

	

	def create(self):
		self.y = 20
		self.sy = 1
		self.usePixels()


	def update(self, delta):
		if(self.y >= 30 and self.sy == 1):
			 self.sy = -1
			 
		if(self.y <= 5 and self.sy == -1):
			 self.sy = 1
			 
		self.y += self.sy

	def render(self):
		self.clpx()
		self.pixel(120, 80, 2)
		
		self.drawColor(2) #only pixel takes a color, so we set red for the following
		self.rect(10, 100, 45, 45)
		self.rect(60, 100, 45, 45, True) # full rect
		
		self.drawColor(9)
		self.circle(45, self.y, 5)
		self.circle(60, 20, 5, True)
		
		self.drawColor(5)
		self.triangle(5, 5, 5, 35, 20, 5)
		
		self.drawColor(1)
		self.line(5, 50, 120, 55)
	
