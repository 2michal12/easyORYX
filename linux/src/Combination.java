import java.util.Arrays;

/**
 *
 * @author Michal
 */
public class Combination {
    
    private int count;
    private int val;
    private int high8;
    private String s;

    public Combination(){
    }
    
    public int combination_bits(int[] a, int[] b, int[] k, int keystream){
        //printArray(a); printArray(b); printArray(k);
        count = 0;
        /*print_high(k);
        print_high(a);
        print_high(b);*/
        
        shift_left(k, 1);
        shift_left(a, 1);
        if(k[6] == 0){ //8 moznosti
            shift_left(b, 1); //doplna 1 bit do high_b
            // k[0]=1
            k[7]=1; a[7]=1; b[7]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // k[0]=1
            k[7]=1; a[7]=1; b[7]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            // k[0]=1
            k[7]=1; a[7]=0; b[7]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // k[0]=1
            k[7]=1; a[7]=0; b[7]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            // k[0]=0
            k[7]=0; a[7]=1; b[7]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // k[0]=0
            k[7]=0; a[7]=1; b[7]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            // k[0]=0
            k[7]=0; a[7]=0; b[7]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // k[0]=0
            k[7]=0; a[7]=0; b[7]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            //--
            shift_right(b, 1);
        }else{ //16 moznosti
            shift_left(b, 2); //doplna 2 bity do high_b
            // k[0]=1 a[0]=1
            k[7]=1; a[7]=1; b[7]=1; b[6]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=1; a[7]=1; b[7]=1; b[6]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=1; a[7]=1; b[7]=0; b[6]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=1; a[7]=1; b[7]=0; b[6]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            
            // k[0]=1 a[0]=0
            k[7]=1; a[7]=0; b[7]=1; b[6]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=1; a[7]=0; b[7]=1; b[6]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=1; a[7]=0; b[7]=0; b[6]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=1; a[7]=0; b[7]=0; b[6]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            
            // k[0]=0 a[0]=1
            k[7]=0; a[7]=1; b[7]=1; b[6]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=0; a[7]=1; b[7]=1; b[6]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=0; a[7]=1; b[7]=0; b[6]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=0; a[7]=1; b[7]=0; b[6]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            
            // k[0]=0 a[0]=0
            k[7]=0; a[7]=0; b[7]=1; b[6]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=0; a[7]=0; b[7]=1; b[6]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=0; a[7]=0; b[7]=0; b[6]=1;
            count += test(a, b, k, keystream) ? 1 : 0;
            // 
            k[7]=0; a[7]=0; b[7]=0; b[6]=0;
            count += test(a, b, k, keystream) ? 1 : 0;
            //--
            shift_right(b, 2);
        }
        shift_right(a, 1);
        shift_right(k, 1);

        /*print_high(k);
        print_high(a);
        print_high(b);*/
        
        return count;
    }
    
    private boolean test(int[] a, int[] b, int[] k, int keystream){
        val = (toInt(k) + 255-toInt(a) + 255-toInt(b)) % 256 ; 
        //System.out.println(val);
        if(val == keystream){
            return true;
        } 
        return false;
    }
    
    private void shift_right(int[] high8, int pos){
        for(int i = 0; i < pos; i++){
            for(int j = high8.length-1; j > 0; j--){
                high8[j] = high8[j-1];
            }
        }
    }
    
    private void shift_left(int[] high8, int pos){
        for(int i = 0; i < pos; i++){
            for(int j = 0; j < high8.length-1; j++){
                high8[j] = high8[j+1];
            }
        }
    }
    
    private int toInt(int[] lfsr){
        high8 = (int)(lfsr[0]*Math.pow(2.0, 7.0) + lfsr[1]*Math.pow(2.0, 6.0) + lfsr[2]*Math.pow(2.0, 5.0) + lfsr[3]*Math.pow(2.0, 4.0) + lfsr[4]*Math.pow(2.0, 3.0) + lfsr[5]*Math.pow(2.0, 2.0) + lfsr[6]*2 + lfsr[7]);
        return high8;
    }
    
    private void print_high(int[] register){
        for(int i = 0; i < register.length; i++){
            if(i < register.length-1)
                System.out.print(register[i]+", ");
            else
                System.out.println(register[i]);
        }
    }
    
    private void printArray(int[] arr){
        for(int i = 0; i < arr.length; i++){
            System.out.print(arr[i]);
        }
        System.out.println();
    }
    
    private void compare_arrays(int[] a, int[] b, int[] k){
        /*
        int[] k_init = {0,0,1,0,1,0,1,0,1,0,1,1,1,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0};
        int[] a_init = {1,0,1,0,1,0,0,1,1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,1,0,1,1,0,1,1};
        int[] b_init = {1,1,1,0,1,0,0,1,1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,1,0,1,1,0,0,0};
        83  0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1
        166 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1
        85  0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1
        */
        if(a[0] == 0 && a[1] == 1 && a[2] == 0 && a[3] == 1 && a[4] == 0 && a[5] == 0 && a[6] == 1 && a[7] == 1){
            if(b[0] == 1 && b[1] == 0 && b[2] == 1 && b[3] == 0 && b[4] == 0 && b[5] == 1 && b[6] == 1 && b[7] == 0){
                if(k[0] == 0 && k[1] == 1 && k[2] == 0 && k[3] == 1 && k[4] == 0 && k[5] == 1 && k[6] == 0 && k[7] == 1){
                    printArray(a);
                    printArray(b);
                    printArray(k);
                } 
            }    
        }
    }
}
