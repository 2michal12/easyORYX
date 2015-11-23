package nks3;



/**
 *
 * @author Michal
 */
public class Main {
    
    public static void main(String[] args) {

        // 1.
        int[] k_init = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1};
        int[] a_init = {1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0};
        int[] b_init = {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0};
        /*int[] k_init = {0,0,1,0,1,0,1,0,1,0,1,1,1,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0};
        int[] a_init = {1,0,1,0,1,0,0,1,1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,1,0,1,1,0,1,1};
        int[] b_init = {1,1,1,0,1,0,0,1,1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,1,0,1,1,0,0,0};*/
        LFSR lfsr_k = new LFSR(k_init);
        LFSR lfsr_a = new LFSR(a_init);
        LFSR lfsr_b = new LFSR(b_init);  
        
        System.out.print("Initial value of registers: \nLFSR_K: ");
        lfsr_k.print_lfsr();
        System.out.print("LFSR_A: ");
        lfsr_a.print_lfsr();
        System.out.print("LFSR_B: ");
        lfsr_b.print_lfsr();
                
        // 2.
        lfsr_k.shift_register(1, "LFSR_K");
        
        // 3.
        if(lfsr_k.get_item(0) == 0){ //ak je LFSR_K[0]==0 (teda ak k1==0)
            lfsr_a.shift_register(1, "LFSR_A0"); //tak LFSR_A sa posunie o 1 bit podla LFSR_A0
        }else{
            lfsr_a.shift_register(1, "LFSR_A1"); //inak podľa LFSR_A1
        }
        
        // 4.
        if(lfsr_k.get_item(1) == 0){ //ak je LFSR_K[1]==0 (teda ak k2==0)
            lfsr_b.shift_register(1, "LFSR_B"); //tak LFSR_B sa posunie o 1 bit
        }else{
            lfsr_b.shift_register(2, "LFSR_B"); //inak o 2 bity
        }
        
        // 5. keystream_byte1={High8_K + L(High8_A) + L(High8_B)} mod 256 pouzity S-BOX L(x) = 255 - x, kde 0<=x<=255
        int keystream_byte1;
        
        keystream_byte1 = ( (int)lfsr_k.high8() + 255-(int)lfsr_a.high8() + 255-(int)lfsr_b.high8() ) % 256;
        
        // 6.
        lfsr_k.shift_register(1, "LFSR_K");
        
        // 7.
        if(lfsr_k.get_item(0) == 0){ //ak je LFSR_K[0]==0 (teda ak k1==0)
            lfsr_a.shift_register(1, "LFSR_A0"); //tak LFSR_A sa posunie o 1 bit podla LFSR_A0
        }else{
            lfsr_a.shift_register(1, "LFSR_A1"); //inak podľa LFSR_A1
        }
        
        // 8.
        if(lfsr_k.get_item(1) == 0){ //ak je LFSR_K[1]==0 (teda ak k2==0)
            lfsr_b.shift_register(1, "LFSR_B"); //tak LFSR_B sa posunie o 1 bit
        }else{
            lfsr_b.shift_register(2, "LFSR_B"); //inak o 2 bity
        }
        
        // 9. keystream_byte1={High8_K + L(High8_A) + L(High8_B)} mod 256
        int keystream_byte2;
        keystream_byte2 = ( (int)lfsr_k.high8() + 255-(int)lfsr_a.high8() + 255-(int)lfsr_b.high8() ) % 256;
        
         
    // ======================================================================================ATTACK
         
    //keystream_byte1 = 81;
    //keystream_byte2 = 165;
    
    System.out.println("Keystreams: k1= "+keystream_byte1+"  k2= "+keystream_byte2);
    
    int[][]high8_mask_temp = new int[9][256];
    int index = 0;
    int mask;
    
    //vytvorenie masky so zlym indexom, od 1
    for (int i=0;i<256;i++){
        mask = 256;
            while (mask > 0){
                if ((mask & i) == 0){
                    high8_mask_temp[index][i] = 0;
                    index++;
                }else{
                    high8_mask_temp[index][i] = 1;
                    index++;
                }
                mask = mask >> 1;
            }
            index = 0;
    }
    
    //prevod kvoli posunu indexu 
    int[][]high8_mask = new int[8][256];
    for(int i = 0; i < 256; i++){
        for(int j = 0; j < 8; j++){
            high8_mask[j][i] = high8_mask_temp[j+1][i];
        }
    }
    
    int[] temp_k = new int[8];
    int[] temp_a = new int[8];
    int[] temp_b = new int[8];
    int[] temp_b_x = new int[9]; //pomocne pole kvoli prevodu do dvojkovej sustave
    LFSR lfsr_k_temp;
    LFSR lfsr_a_temp;
    LFSR lfsr_b_temp;
    int high8_k_temp;
    int high8_a_temp;
    int l;
    int[] statistics = new int[17];
    int tmp;
    Combination combination = new Combination();
    
    //vynulovat pole pre statistku
    for(int x = 0; x < statistics.length; x++)
        statistics[x] = 0;
       
    for(int i = 0; i < 256; i++){
        
        for(int j = 0; j < 256; j++){

            //vzdy otestovat vsetky moznosti high8_b kedy bude kombinacia vyhovovat pre keystream_byte1
            for(int high8_b_temp = 0; high8_b_temp < 256; high8_b_temp++){
                //naplni temp_a
                for(int a = 0; a < 8; a++){
                    temp_a[a] = high8_mask[a][j];
                }            
                lfsr_a_temp = new LFSR(temp_a);
                //naplni temp_k
                for(int k = 0; k < 8; k++){
                    temp_k[k] = high8_mask[k][i];
                }
                lfsr_k_temp = new LFSR(temp_k);
                
                //pre kazdu iteraciu nove hodnoty registrov high8_k a hight8_a
                high8_k_temp = (int)lfsr_k_temp.high8();
                high8_a_temp = (int)lfsr_a_temp.high8();
                
                if( ( ( high8_k_temp + 255-high8_a_temp  + 255-high8_b_temp ) % 256 ) == keystream_byte1 ){ //testovanie pre keystream_byte1
                    //prevod cisla do bitoveho pola
                    l = high8_b_temp;
                    mask = 256;
                    index = 0;
                    while (mask > 0){
                        if ((mask & l) == 0){
                            temp_b_x[index] = 0;
                            index++;
                        }else{
                            temp_b_x[index] = 1;
                            index++;
                        }
                        mask = mask >> 1;
                    }
                    //posun kvoli zlemu indexovaniu
                    for(int b = 0; b < 8; b++){ 
                        temp_b[b] = temp_b_x[b+1];
                    } 
                    //napln temp_b
                    lfsr_b_temp = new LFSR(temp_b);
                    
                    //hladanie moznosti pre trojicu vyhovujucich registrov  (lfsr_a_temp, lfsr_b_temp, lfsr_k_temp)
                    
                    //1. moznost automatickeho testovania poradia bitov                     
                    lfsr_k_temp.shift_left(1);
                    lfsr_a_temp.shift_left(1);
                    
                    if( lfsr_k_temp.get_item(1) == 0 ){
                        lfsr_b_temp.shift_left(1);
                                                
                        tmp = lfsr_b_temp.combination(8, lfsr_a_temp, lfsr_b_temp, lfsr_k_temp, keystream_byte2);  
                    }else{
                        lfsr_b_temp.shift_left(2);
                        
                        tmp = lfsr_b_temp.combination(16, lfsr_a_temp, lfsr_b_temp, lfsr_k_temp, keystream_byte2);  
                    }
                    statistics[tmp]++;
                    
                    //2. moznost manualneho testovania poradia bitov
                    //tmp = combination.combination_bits(temp_a, temp_b, temp_k, keystream_byte2);   
                    //statistics[tmp] += 1;
                    
                
                }
            }        
        }
    }
    
    // ======================================================================================STATs

    for(int i = 0; i < statistics.length; i++){
        System.out.println(i+". moznosti kombinacie bitov: "+statistics[i]);
    }
    
    } //end of main function
        
}
