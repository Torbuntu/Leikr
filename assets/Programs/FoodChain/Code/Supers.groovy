class Supers {
    //USE MEAT SUPER. ta=type A, tb = type B
    def useSuper(jar, ta, tb){
    	int tmp = 0
    	8.times{i->
    		8.times{j->
    			if(jar[i][j].type == ta || jar[i][j].type == tb){
    				jar[i][j].type = 8
    				tmp++
    			}				
    		}
    	}
    	return tmp
    }
    
}
