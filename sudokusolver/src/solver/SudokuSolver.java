package solver;
public class SudokuSolver{
    public static int[][][] squ = new int[9][3][3];
    public static int[][] row = new int[9][9];
    public static int[][] col = new int[9][9];
    public static int[][] preset = {{0,5,0,2,0,0,0,0,0},{0,0,0,4,9,3,0,0,6},{0,0,1,0,0,0,4,0,0},{0,1,0,0,0,0,8,0,0},{0,4,0,0,8,0,0,3,0},{2,8,0,0,0,0,0,6,0},{0,0,0,0,0,0,8,0,0},{1,0,0,5,3,2,0,0,0},{0,0,0,0,0,7,0,1,0}};
    public static boolean canModify = false;

public static void printexc(String exc){
       System.out.println("[Exception]: "+exc);
}

public static void syncSqus(){
       for(int c = 0; c < 9; c++){
       for(int a = 0; a < 3; a++){
               for(int b = 0; b < 3; b++){
                       switch(c){
                               case 0:
                               case 1:
                               case 2:
                                               squ[c][a][b] = row[a+(3*c)][b];
                                    break;
                                    case 3:
                                    case 4:
                                    case 5:
                                            squ[c][a][b] = row[a+(3*(c-3))][b+3];
                                    break;
                                    case 6:
                                    case 7:
                                    case 8:
                                            squ[c][a][b] = row[a+(3*(c-6))][b+6];
                                    break;
                       }
               }
       }
       }
}

	public static boolean noChoices(int a, int b){
		
	}

public static int squThatHasRowCol(int a, int b){
       switch(a){
               case 0:
               case 1:
               case 2:
                               switch(b){
                                    case 0:
                                    case 1:
                                    case 2:
                                            return 0;
                                    case 3:
                                    case 4:
                                    case 5:
                                            return 3;
                                    case 6:
                                    case 7:
                                    case 8:
                                            return 6;
                            }
                    break;
                    case 3:
                    case 4:
                    case 5:
                            switch(b){
                                    case 0:
                                    case 1:
                                    case 2:
                                            return 1;
                                    case 3:
                                    case 4:
                                    case 5:
                                            return 4;
                                    case 6:
                                    case 7:
                                    case 8:
                                            return 7;
                            }
                    break;
                    case 6:
                    case 7:
                    case 8:
                            switch(b){
                                    case 0:
                                    case 1:
                                    case 2:
                                            return 2;
                                    case 3:
                                    case 4:
                                    case 5:
                                            return 5;
                                    case 6:
                                    case 7:
                                    case 8:
                                            return 8;
                            }
                    break;
       }
       return 0;
}

public static boolean squContainsNum(int a, int b){
       for(int i = 0; i < 4; i++){
               for(int j = 0; j < 4; j++){
                       if(squ[a][i][j]==b){
                               return true;
                       }
               }
       }
       return false;
}

public static boolean squContainsNumInRow(int a, int b, int c){
       for(int i = 0; i < 4; i++){
               if(squ[a][b][i]==c){
                       return true;
               }
       }
       return false;
}

    public static boolean rowContainsNum(int a, int b){
            for(int c = 0; c < row[a].length;c++){
                    /**if(row[a][c]==0||canModify){
                            row[a][c] = getPossibleNumberMethod1(a,c);
                    }**/
                    if(row[a][c]==b){
                            return true;
                    }
            }
            return false;
    }
    
    public static boolean rowContainsNumDiscluding(int a, int b, int e, int f){
            for(int c = 0; c < row[a].length;c++){
                    /**if(row[a][c]==0||canModify){
                            row[a][c] = getPossibleNumberMethod1(a,c);
                    }**/
                    if(row[a][c]==b && a!=e && c!=f){
                            return true;
                    }
            }
            return false;
    }
    
    public static boolean colContainsNum(int a, int b){
            for(int c = 0; c < col[a].length; c++){
                    /**if(col[a][c]==0||canModify){
                            col[a][c] = getPossibleNumberMethod1(a,c);
                    }**/
                    if(col[a][c]==b){
                            return true;
                    }
            }
            return false;
    }
    
    public static boolean colContainsNumDiscluding(int a, int b, int e, int f){
            for(int c = 0; c < col[a].length; c++){
                    /**if(col[a][c]==0||canModify){
                            col[a][c] = getPossibleNumberMethod1(a,c);
                    }**/
                    if(col[a][c]==b && a!=e && c!=f){
                            return true;
                    }
            }
            return false;
    }
//Wondering if I should just remove this. I no longer have any use for it, but one might come up in future.
    public static boolean isSurrounded(int i, int a){
            int conf = 0;
            try{
            if(row[i-1][a-1]!=0){
                    conf++;
            }
            if(row[i-1][a]!=0){
                    conf++;
            }
            if(row[i-1][a+1]!=0){
                    conf++;
            }
            if(row[i][a-1]!=0){
                    conf++;
            }
            if(row[i][a+1]!=0){
                    conf++;
            }
            if(row[i+1][a-1]!=0){
                    conf++;
            }
            if(row[i+1][a]!=0){
                    conf++;
            }
            if(row[i+1][a+1]!=0){
                    conf++;
            }
            }catch(Exception e){
            }
            if(conf==8){
                    return true;
            }
            return false;
    }
    //WORKING ON, NOT COMPLETE YET.
                    

    public static int getPossibleNumberMethod2(int i, int a){
    for(int z = 8; z > 0; z--){ //I want this to go to 9. Layout correct?
        if(i>=1 && a>=1 && i<8 && a<8){
                try{
                        if(rowContainsNum(i-1, z)||rowContainsNum(i+1, z)||colContainsNum(a-1,z)||colContainsNum(a+1,z)){
                                if(!rowContainsNumDiscluding(i,z,i,a)){
                                        if(!colContainsNumDiscluding(a,z,a,i)){
                                        	while(rowContainsNum(i,z)||colContainsNum(a,z)){
                                    			if(z!=1){
                                    				z--;
                                    			}
                                    			else{
                                    				z = 9;
                                    			}
                                    		}
                                    		return z;
                                        }
                                }
                        }
                }catch(NullPointerException npe1){
                        printexc(""+npe1);
                }
        }
        else if(i<8 && a<8){
                try{
                        if(rowContainsNum(i+1, z)||colContainsNum(a+1,z)){
                                if(!rowContainsNumDiscluding(i,z,i,a)){
                                        if(!colContainsNumDiscluding(a,z,a,i)){
                                        	while(rowContainsNum(i,z)||colContainsNum(a,z)){
                                    			if(z!=1){
                                    				z--;
                                    			}
                                    			else{
                                    				z = 9;
                                    			}
                                    		}
                                    		return z;
                                        }
                                }
                        }
                }catch(NullPointerException npe2){
                        printexc(""+npe2);
                }
        }
        else if(i>=1 && a>=1){
                try{
                        if(rowContainsNum(i-1, z)||colContainsNum(a-1,z)){
                                if(!rowContainsNumDiscluding(i,z,i,a)){
                                        if(!colContainsNumDiscluding(a,z,a,i)){
                                        	while((rowContainsNum(i,z)||colContainsNum(a,z))){
                                    			if(z!=1){
                                    				z--;
                                    			}
                                    			else{
                                    				z = 9;
                                    			}
                                    		}
                                    		return z;
                                        }
                                }       
                        }
                }catch(NullPointerException npe3){
                        printexc(""+npe3);
                }
        }
}
return 0;
    }
       
    public static int getPossibleNumberMethod1(int i, int a){
            for(int z = 1; z < 10; z++){ //I want this to go to 9. Layout correct?
                    if(i>=1 && a>=1 && i<8 && a<8){
                            try{
                                    if(rowContainsNum(i-1, z)||rowContainsNum(i+1, z)||colContainsNum(a-1,z)||colContainsNum(a+1,z)){
                                            if(!rowContainsNumDiscluding(i,z,i,a)){
                                                    if(!colContainsNumDiscluding(a,z,a,i)){
                                                    		while(rowContainsNum(i,z)||colContainsNum(a,z)){
                                                    			if(z!=9){
                                                    				z++;
                                                    			}
                                                    			else{
                                                    				z = 1;
                                                    			}
                                                    		}
                                                    		return z;
                                                    }
                                            }
                                    }
                            }catch(NullPointerException npe1){
                                    printexc(""+npe1);
                            }
                    }
                    else if(i<8 && a<8){
                            try{
                                    if(rowContainsNum(i+1, z)||colContainsNum(a+1,z)){
                                            if(!rowContainsNumDiscluding(i,z,i,a)){
                                                    if(!colContainsNumDiscluding(a,z,a,i)){
                                                    	while(rowContainsNum(i,z)||colContainsNum(a,z)){
                                                			if(z!=9){
                                                				z++;
                                                			}
                                                			else{
                                                				z = 1;
                                                			}
                                                		}
                                                		return z;
                                                    }
                                            }
                                    }
                            }catch(NullPointerException npe2){
                                    printexc(""+npe2);
                            }
                    }
                    else if(i>=1 && a>=1){
                            try{
                                    if(rowContainsNum(i-1, z)||colContainsNum(a-1,z)){
                                            if(!rowContainsNumDiscluding(i,z,i,a)){
                                                    if(!colContainsNumDiscluding(a,z,a,i)){
                                                    	while(rowContainsNum(i,z)||colContainsNum(a,z)){
                                                			if(z!=9){
                                                				z++;
                                                			}
                                                			else{
                                                				z = 1;
                                                			}
                                                		}
                                                		return z;
                                                    }
                                            }       
                                    }
                            }catch(NullPointerException npe3){
                                    printexc(""+npe3);
                            }
                    }
            }
            return 0;
    }
    
    public static boolean completeRow(int a){
            if(canCompleteRow(a)){
                    for(int i = 0; i < row[a].length; i++){
                            if(row[a][i]==0){
                                    row[a][i]=getPossibleNumberMethod1(a,i);
                                    return true;
                            }
                    }
            }
            return false;
    }
    
    public static boolean completeCol(int a){
            if(canCompleteCol(a)){
                    for(int i = 0; i < col[a].length; i++){
                            if(col[a][i]==0){
                                    col[a][i]=getPossibleNumberMethod1(a,i);
                                    return true;
                            }
                    }
            }
            return false;
    }
    
    public static boolean canCompleteRow(int a){
            int[] b = new int[9];
            for(int i = 0; i < row[a].length; i++){
                    if(row[a][i]!=0){
                            switch(row[a][i]){
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                            if(b[row[a][i]]!=0){
                                                    return false;
                                            }else{
                                                    b[row[a][i]]=1;
                                            }
                                    break;
                            }
                    }
            }
            if((b[0]+b[1]+b[2]+b[3]+b[4]+b[5]+b[6]+b[7]+b[8])==8){
                    return true;
            }
            return false;
    }

public static boolean canCompleteCol(int a){
            int[] b = new int[9];
            for(int i = 0; i < col[i].length; i++){
                    if(col[i][a]!=0){
                            switch(col[i][a]){
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                            if(b[col[i][a]]!=0){
                                                    return false;
                                            }else{
                                                    b[col[i][a]]=1;
                                            }
                                    break;
                            }
                    }
            }
            if((b[0]+b[1]+b[2]+b[3]+b[4]+b[5]+b[6]+b[7]+b[8])==8){
                    return true;
            }
            return false;
    }

    public static boolean checkSolved(){
            int isTrue = 0;
            for(int a = 0; a < 9; a++){
                    for(int b = 0; b < row[a].length; b++){
                            if(row[a][b]!=0){
                                    isTrue++;
                            }
                    }
            }
            if(isTrue == 81){
                    for(int j = 0; j < col[0].length; j++){
                            try{
                                    System.out.println(" DONE ");
                                       System.out.println(col[0][j]+" "+col[1][j]+" "+col[2][j]+" "+col[3][j]+" "+col[4][j]+" "+col[5][j]+" "+col[6][j]+" "+col[7][j]+" "+col[8][j]);
                            }catch(NullPointerException npe){
                                    printexc(""+npe);
                            }
                    }
                    return true;
            }
            return false;
    }
    
    public static void loadPresets(){
            for(int i = 0; i < 9; i++){
                    for(int j = 0; j < 9; j++){
                            if(preset[i][j]!=0){
                                    row[i][j]=preset[i][j];
                                    col[j][i]=preset[i][j];
                            }
                    }
            }
    }
    
    public static void syncRowsWCol(){
            for(int a = 0; a < 9; a++){
                    for(int b = 0; b < 9; b++){
                            row[a][b]=col[b][a];
                    }
            }
    }
    
    public static void syncColWRows(){
            for(int a = 0; a < 9; a++){
                    for(int b = 0; b < 9; b++){
                            col[a][b]=row[b][a];
                    }
            }
    }
    
    /**public static void cleanLostRowColMem(){
            for(int a = 0; a < 9; a++){
                    for(int b = 0; b < 9; b++){
                            if(row[a][b]!=col[b][a]||row[a][b]==null||col[b][a]==null){
                                    row[a][b]=0;
                                    col[b][a]=0;
                            }
                    }
            }
    }**/
              
    public static void solve(){
            int startAt = 0;
            int pos = 0;
            boolean solved = false;
            /**for(int o = 0; o < row.length; o++){
                    for(int p = 0; p < col[o].length; p++){
                            if(row[o][p]= null){
                                    row[o][p]=0;
                            }
                            if(col[o][p]==null){
                                    col[o][p]=0;
                            }
                    }
            }**/
            loadPresets();
            while(!solved){
                    for(int i = startAt; i < 9; i++){
                            for(int a = pos; a < 9; a++){
                                    if(row[i][a]==0||col[a][i]==0){
                                            row[i][a]=getPossibleNumberMethod1(i,a);
                                            col[a][i]=getPossibleNumberMethod1(i,a);
                                            //syncColWRows();
                                    }
                                    /**if(col[a][i]==0||row[i][a]==0){
                                            col[a][i]=getPossibleNumberMethod2(i,a);
                                            //syncRowsWCol();
                                    }**/
                                    loadPresets();
                            }
                            loadPresets();
                    }
                    for(int j = 0; j < 9; j++){
                            try{
                                    System.out.println(" ");
                                       System.out.println(col[0][j]+" "+col[1][j]+" "+col[2][j]+" "+col[3][j]+" "+col[4][j]+" "+col[5][j]+" "+col[6][j]+" "+col[7][j]+" "+col[8][j]);
                            }catch(NullPointerException npe){
                                    printexc(""+npe);
                            }
                    }
                    solved = checkSolved();
                    //syncRowsWCol();
                    //syncColWRows();
                    //cleanLostRowColMem();
            }
    }
    public static void main(String[] args){
            solve();
    }
}