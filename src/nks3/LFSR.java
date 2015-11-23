package nks3;



/**
 *
 * @author Michal
 */

public class LFSR {
    public int i,j;
    public int[] lfsr = null;
    public int s31;
    private double high8;
    private int[] bin;
    private int val;
    private int counter;
    
    public LFSR(int[] init){
        this.lfsr = new int[init.length];
        for(i = 0; i < init.length; i++){
            lfsr[i] = init[i];
        }
    }
    
    private void polynom_k(int count){
        //1+x+x^5+x^6+x^9+x^10+x^11+x^14+x^16+x^18+x^19+x^28+x^32       
        for(j = 0; j < count; j++){
            s31 = 1^lfsr[0]^lfsr[4]^lfsr[5]^lfsr[8]^lfsr[9]^lfsr[10]^lfsr[13]^lfsr[15]^lfsr[17]^lfsr[18]^lfsr[27]^lfsr[31];
            for(i = 0; i < lfsr.length-1; i++){
                lfsr[i] = lfsr[i+1];
            }
            lfsr[31] = s31;
        }
    }
    
    private void polynom_a0(int count){
        //1+x+x^2+x^4+x^5+x^7+x^8+x^10+x^11+x^12+x^16+x^22+x^23+x^26+x^32       
        for(j = 0; j < count; j++){
            s31 = 1^lfsr[0]^lfsr[1]^lfsr[3]^lfsr[4]^lfsr[6]^lfsr[7]^lfsr[9]^lfsr[10]^lfsr[11]^lfsr[15]^lfsr[21]^lfsr[22]^lfsr[25]^lfsr[31]; 
            for(i = 0; i < lfsr.length-1; i++){
                lfsr[i] = lfsr[i+1];
            }
            lfsr[31] = s31;
        }
    }
    
    private void polynom_a1(int count){
        //1+x+x^2+x^7+x^8+x^9+x^10+x^11+x^13+x^17+x^22+x^23+x^24+x^25+x^26+x^27+x^32       
        for(j = 0; j < count; j++){
            s31 = 1^lfsr[0]^lfsr[1]^lfsr[6]^lfsr[7]^lfsr[8]^lfsr[9]^lfsr[10]^lfsr[12]^lfsr[16]^lfsr[21]^lfsr[22]^lfsr[23]^lfsr[24]^lfsr[25]^lfsr[26]^lfsr[31]; 
            for(i = 0; i < lfsr.length-1; i++){
                lfsr[i] = lfsr[i+1];
            }
            lfsr[31] = s31;
        }
    }
    
    private void polynom_b(int count){
        //1+x+x^3+x^6+x^15+x^16+x^20+x^21+x^31+x^32      
        for(j = 0; j < count; j++){
            s31 = 1^lfsr[0]^lfsr[2]^lfsr[5]^lfsr[14]^lfsr[15]^lfsr[19]^lfsr[20]^lfsr[30]^lfsr[31]; 
            
            for(i = 0; i < lfsr.length-1; i++){
                lfsr[i] = lfsr[i+1];
            }
            lfsr[31] = s31;
        }
    }
    
    public void shift_register(int count, String register){
        switch(register){
            case "LFSR_K":
                polynom_k(count);
                break;
            case "LFSR_A0":
                polynom_a0(count);
                break;
            case "LFSR_A1":
                polynom_a1(count);                
                break;
            case "LFSR_B":
                polynom_b(count);
                break;
            default:
                break;
             
        }
    }
    
    public double high8(){
        //High8_K = 2^7*k1+2^6*k2+2^5*k3+2^4*k4+2^3*k5+2^2*k6+2*k7+k8
        high8 = this.lfsr[0]*Math.pow(2.0, 7.0) + this.lfsr[1]*Math.pow(2.0, 6.0) + this.lfsr[2]*Math.pow(2.0, 5.0) + this.lfsr[3]*Math.pow(2.0, 4.0) + this.lfsr[4]*Math.pow(2.0, 3.0) + this.lfsr[5]*Math.pow(2.0, 2.0) + this.lfsr[6]*2 + this.lfsr[7];
        return high8;
    }
    
    public int get_item(int index){
        return this.lfsr[index];
    }
    
    public void set_item(int index, int value){
        this.lfsr[index] = value;
    }
    
    public void print_lfsr(){
        for(i = 0; i < this.lfsr.length; i++)
            if(i != this.lfsr.length-1)
                System.out.print(this.lfsr[i] + ", ");
            else
                System.out.print(this.lfsr[i]);
        System.out.println();
    }
    
    public void print_lfsr_high8(){
        for(i = 0; i < this.lfsr.length-1; i++)
            if(i != this.lfsr.length-2)
                System.out.print(this.lfsr[i] + ", ");
            else
                System.out.print(this.lfsr[i]);
        System.out.println();
    }
    
    //--- ATTACK functions
    
    public void shift_left(int pos){
        for(int i = 0; i < pos; i++){
            for(int j = 0; j < this.lfsr.length-1; j++){
                this.lfsr[j] = this.lfsr[j+1];
            }
        }
    }
    
    public int combination(int type, LFSR a, LFSR b, LFSR k, int keystream2){
        counter = 0;
        if( type == 8 ){
            for(int i = 0; i < 8; i++){
                bin = convert_binary_8(i);
                
                a.set_item(7, bin[1]);
                k.set_item(7, bin[2]);
                b.set_item(7, bin[3]);
                
                if( test(a, b, k, keystream2) )
                    counter++;
            }
        }else{
            for(int i = 0; i < 16; i++){
                bin = convert_binary_8(i);
                
                a.set_item(7, bin[0]);
                k.set_item(7, bin[1]);
                b.set_item(6, bin[2]);
                b.set_item(7, bin[3]);

                if( test(a, b, k, keystream2) )
                    counter++;
            }
        }
        return counter;
        
    }
    
    private boolean test(LFSR a, LFSR b, LFSR k, int keystream2){
        val = ((int)k.high8() + 255-(int)a.high8() + 255-(int)b.high8() ) % 256 ;
        if(val == keystream2){
            return true;
        } 
        return false;
    }
    
    private static int[] convert_binary_8(int no) {
    int binary[];
  
    if(no == 0){
        binary = new int[4];
        binary[0] = 0;
        binary[1] = 0;
        binary[2] = 0;
        binary[3] = 0;
    }else if(no == 1){
        binary = new int[4];
        binary[0] = 0;
        binary[1] = 0;
        binary[2] = 0;
        binary[3] = 1;
    }else if(no == 2){
        binary = new int[4];
        binary[0] = 0;
        binary[1] = 0;
        binary[2] = 1;
        binary[3] = 0;
    }else if(no == 3){
        binary = new int[4];
        binary[0] = 0;
        binary[1] = 0;
        binary[2] = 1;
        binary[3] = 1;
    }else if(no == 4){
        binary = new int[4];
        binary[0] = 0;
        binary[1] = 1;
        binary[2] = 0;
        binary[3] = 0;
    }else if(no == 5){
        binary = new int[4];
        binary[0] = 0;
        binary[1] = 1;
        binary[2] = 0;
        binary[3] = 1;
    }else if(no == 6){
        binary = new int[4];
        binary[0] = 0;
        binary[1] = 1;
        binary[2] = 1;
        binary[3] = 0;
    }else if(no == 7){
        binary = new int[4];
        binary[0] = 0;
        binary[1] = 1;
        binary[2] = 1;
        binary[3] = 1;
    }else{
        int i = 0, temp[] = new int[7];
        while (no > 0) {
            temp[i++] = no % 2;
            no /= 2;
        }
        binary = new int[i];
        int k = 0;
        for (int j = i - 1; j >= 0; j--) {
            binary[k++] = temp[j];
        }
    }
    
    return binary;
    }
    
}
