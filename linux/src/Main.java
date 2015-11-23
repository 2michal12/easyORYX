

/**
 *
 * @author Michal
 */
public class Main {
    
    public static void main(String[] args) {
   
        
        // 1.
        int[] k_init = {0,0,1,0,1,0,1,0,1,0,1,1,1,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0};
        int[] a_init = {1,0,1,0,1,0,0,1,1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,1,0,1,1,0,1,1};
        int[] b_init = {1,1,1,0,1,0,0,1,1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,1,0,1,1,0,0,0};
        /*int[] k_init =   {0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0};
        int[] a_init =   {0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] b_init  =   {0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0};*/
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
        System.out.print((int)lfsr_k.high8()+"  ");
        lfsr_k.print_lfsr();
        System.out.print((int)lfsr_a.high8()+"  ");
        lfsr_a.print_lfsr();
        System.out.print((int)lfsr_b.high8()+"  ");
        lfsr_b.print_lfsr();
        
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
    int[] output_statistic = new int[65536];
    int index_statistic = 0;
    Combination combination = new Combination();
    
    for(int i = 0; i < 256; i++){
        
         for(int a = 0; a < 8; a++){
            temp_a[a] = high8_mask[a][i];
         }            
         lfsr_a_temp = new LFSR(temp_a);

        for(int j = 0; j < 256; j++){
            
            for(int k = 0; k < 8; k++){
                temp_k[k] = high8_mask[k][j];
            }
            lfsr_k_temp = new LFSR(temp_k);
             
            //pre kazdu iteraciu nove hodnoty registrov high8_k a hight8_a
            high8_k_temp = (int)lfsr_k_temp.high8();
            high8_a_temp = (int)lfsr_a_temp.high8();
            
            //vzdy otestovat vsetky moznosti high8_b kedy bude kombinacia vyhovovat pre keystream_byte1
            for(int high8_b_temp = 0; high8_b_temp < 256; high8_b_temp++){
                if( ( ( high8_a_temp + 255-high8_k_temp + 255-high8_b_temp ) % 256 ) == keystream_byte1 ){
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
                    
                    //vytvorenie registra pre vyhovujuce high8_temp po prevode do 2sustavy
                    lfsr_b_temp = new LFSR(temp_b);
                    
                    //hladanie moznosti pre trojicu vyhovujucich registrov  (lfsr_a_temp, lfsr_b_temp, lfsr_k_temp)
                    output_statistic[index_statistic] = combination.combination_bits(temp_a, temp_b, temp_k, keystream_byte2);
                    index_statistic++;
                    //if(l != 0)
                        //System.out.println(l);
                        //System.out.println( combination.combination_bits(temp_a, temp_b, temp_k, keystream_byte2) );
                }
            }
            //System.out.println();         
        }
    }
    
    // ======================================================================================STATs
    int count_of_0 = 0;
    int count_of_1 = 0;
    int count_of_3 = 0;
    int count_of_4 = 0;

    for(int i = 0; i < output_statistic.length; i++){
        if( output_statistic[i] == 0 )
            count_of_0++;
        else if( output_statistic[i] == 1 )
            count_of_1++;
        else if( output_statistic[i] == 3 )
            count_of_3++;
        else if( output_statistic[i] == 4 )
            count_of_4++;
        else
            System.out.println(output_statistic[i]);
    }
    count_of_0 = output_statistic.length - count_of_0;
    System.out.println("Kombinacie registrov vyhovujucich pre keystream_byte2: "+count_of_0);
    System.out.println("1. moznost kombinacie bitov: "+count_of_1);
    System.out.println("3. moznosti kombinacie bitov: "+count_of_3);
    System.out.println("4. moznost kombinacie bitov: "+count_of_4);    
    
    } //end of main function
}
