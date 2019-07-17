import Help
class Test{
	def h = new Help()
	def getTest(){		
		return "This is Test.\nThe value of X in Help:\n `$h.x`\nAnd y:\n $h.y"
	}
}
