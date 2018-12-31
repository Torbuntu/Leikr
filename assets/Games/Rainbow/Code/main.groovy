import leikr.Engine;
class Rainbow extends Engine{
	def t;
	void create(){
		t = 0
	}
	void update(){}
	void render(){
		t+=1;
		int am=10;
		for(int i=1; i<=am;i++){
			def a=(i/am)+(t/300);
			def x=Math.cos(a)*32+64;
			def y=Math.sin(a)*32+64;

			//circfill(x,y,3,8)

			shp(x,y,32+Math.sin(t/50)*10, Math.max(1,Math.sin(t/100)*3+10), Math.floor(Math.abs(Math.cos(t/300)*5))+3, t/100+i/10);
		}
	}
	
	
	
	def shp(x,y,r,c,n,a){
		a=a>=0?a:0;
		def s=1/n;

		for(int i = 1; i <= n; i++){
			def a2=a+s*i;
			def (x1,y1) = calc(x,y,r,a2);
			def (x2,y2) = calc(x,y,r,a2-s);
			line(x1.toFloat(),y1.toFloat(),x2.toFloat(),y2.toFloat(),c.toInteger());
		}
	}

	def calc(x,y,r,a){
		[Math.cos(a)*r+x, Math.sin(a)*r+y];
	}

}
game = new Rainbow();


