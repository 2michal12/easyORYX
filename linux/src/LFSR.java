

/**
 *
 * @author Michal
 */

public class LFSR {
    public int i,j;
    public int[] lfsr = null;
    public int s31;
    private double high8;
    
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
            
            /*for(i = lfsr.length-1; i > 0; i--){
                lfsr[i] = lfsr[i-1];
            }
            lfsr[0] = s31;*/
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
            
            /*for(i = lfsr.length-1; i > 0; i--){
                lfsr[i] = lfsr[i-1];
            }
            lfsr[0] = s31;*/
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
            
            /*for(i = lfsr.length-1; i > 0; i--){
                lfsr[i] = lfsr[i-1];
            }
            lfsr[0] = s31;*/
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
            /*for(i = lfsr.length-1; i > 0; i--){
                lfsr[i] = lfsr[i-1];
            }
            lfsr[0] = s31;*/
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
    
    public void combine_missing_bits(LFSR a, LFSR b, LFSR k){
        a.print_lfsr_high8();
        b.print_lfsr_high8();
        k.print_lfsr_high8();
    }
    
    public int get_item(int index){
        return this.lfsr[index];
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
}
