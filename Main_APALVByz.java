import java.util.Random;

public class Main_APALVByz {	
	public static final int T = 1; //numero disonesti
	public static int MONETA;
	
	public static final int L = 5*T+1;
	public static final int H = 6*T+1;
	public static int soglia;
	
	//PROCESSI: 0 1 2 3 4 5 6 7 8 (8 è inaffidabile)
	//array dei processi : [ [....], [....], [....], [....] ]
	// struttura per simulare scambio bit tra i processi: [ P0, P1, .... , P8 ] dove, per esempio P2 = [ bitRicevutoDaP0, bitRicevutoDaP1, bitInviatoDiP2, .... ,bitRicevutoDaP8 ] 
	public static int[][] PROCs;

	//array che contiene la scelta che farà il processo i (i = 0, 1, 2...7) al round successivo
	public static int[] NextRoundValue ;
	
	//procedura che inizializza le scelte dei processi
	public static void init() {
		PROCs = new int[9][9];

		NextRoundValue = new int[9]; 
		for(int i = 0; i< 9; i++) {
			var r = new Random();
			NextRoundValue[i] = r.nextInt(2);
		}
		//processo disonesto "invia" -1(non lo invia veramente), solo una notazione per dire che invia messaggi diversi agli altri processi
		NextRoundValue[8] = -1;
		
		for(var x : NextRoundValue) {
			System.out.print(x+" ");
		}
		System.out.println();
		
		for(int i = 0; i<9; i++) {
			PROCs[i][i] = NextRoundValue[i];
		}

	}

	public static int tally(int m, int j) {
		int sum = 0;
		for(int i = 0; i<9;i++) {
			if(PROCs[j][i] == m) sum++;
		}
		
		return sum;
	}
	
	public static int maj(int j) {
		int num0 = 0; //quanti zeri 
		int num1 = 0; //quanti uni
		for(int i = 0; i<9; i++) {
			if(PROCs[j][i] == 0) num0 +=1;
			if(PROCs[j][i] ==1) num1 +=1;			
		}
		
		if(num0>num1) return 0;
		else if (num0<num1)return 1;
		else {//se sono uguali return 0 o 1 a caso
			var r = new Random();
			return r.nextInt(2);
		}
	}
	
	public static void displayProcs() {
		for(var arrayLoc: PROCs) {
			for(var x : arrayLoc) {
				System.out.print(x + " ");
			}
		System.out.print("  |  ");
		}
		System.out.println();
	}
	
	public static void LVbyz(int proc_j) {
			//proc_j PROCESSO ONESTO			
			for(int i = 0; i<9; i++) {			
				PROCs[i][proc_j] = PROCs[proc_j][proc_j]; //trasmissione a tutti i processi
				PROCs[proc_j][i] = PROCs[i][i]; //ricezione da tutti i processi
				
				//azione del disonesto: spedisce il bit 1-(decisione di proc_j) a proc_j
				if(i==8) PROCs[proc_j][8] = 1-PROCs[proc_j][proc_j]; 
			}

			int maj = maj(proc_j);
			int tally = tally(maj, proc_j);
					
			//decisione di proc_j per il round successivo:
			if(MONETA ==1) {
				soglia = L;
			}else {
				soglia = H;
			}
			
			if(tally >= soglia) {
				NextRoundValue[proc_j] = maj;
			}else {
				NextRoundValue[proc_j] = 0;
			}
			
			if(tally >= 7*T+1) NextRoundValue[proc_j] = maj;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static void main(String[] args) {
		System.out.println("VALORI INIZIALI");
		init();
		
		int round = 0;
		while(true) {//inizio round
			var ra = new Random();
			MONETA = ra.nextInt(2);

			//processi onesti + azione del disonesto
			for(int i = 0; i<8; i++) {
				LVbyz(i);			
			}				
			displayProcs();
			//se si trova accordo => fine 
			if(PROCs[0][0]==PROCs[1][1] && PROCs[1][1] == PROCs[2][2] && PROCs[1][1] == PROCs[3][3] && PROCs[1][1] == PROCs[4][4] && PROCs[1][1] == PROCs[5][5] && PROCs[1][1] == PROCs[6][6] && PROCs[1][1] == PROCs[7][7]) break;
			
			//se non si trova accordo => ogni processo (onesto) modifica la propria scelta per il round successivo 
			for(int i = 0; i<8; i++) {
				PROCs[i][i] = NextRoundValue[i];
			}
			
			displayProcs();
			round++;
		}//fine round


}

}
