package it.polimi.dei.provafinale.carcassonne.view.menu;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Font;

public class RulesPanel extends JPanel {

	public RulesPanel() {
		
		String intro;
		intro = "Carcassonne è un gioco a turni da 2 a 5 giocatori in cui ogni giocatore dovrà contribuire alla costruzione di un paesaggio ispirato alla cittadina medievale francese di Carcassonne. Durante il suo turno ogni giocatore ha la possibilità di aggiungere al paesaggio strade, città e campi allo scopo di guadagnare punti vittoria. I punti vittoria serviranno per determinare il vincitore a fine partita. \nQueste regole propongono una versione semplificata della terza edizione del gioco originale. Per chi ha già conoscenza del gioco originale le uniche differenze consistono nell’impossibilità di posizionare segnalini nei campi, nell’assenza di tessere scudo e nell’assenza di tessere monastero.\n\n";
		String materiale_a_disposizione;
		materiale_a_disposizione = "Materiale a disposizione \n-Una pila di tessere territorio quadrate raffiguranti degli elementi che possono essere porzioni di strade, città e campi. \n-7 segnalini di controllo per ogni giocatore: 7 segnalini rossi, 7 segnalini blu, 7 segnalini verdi, 7 segnalini gialli e 7 segnalini neri. \n\n";
		String scopo_del_gioco;
		scopo_del_gioco = "Scopo del gioco \nOgni giocatore posiziona delle tessere territorio e dei segnalini di controllo con lo scopo di guadagnare punti vittoria. Chi, alla fine, avrà totalizzato più punti vittoria risulterà vincitore. \n\n";
		String preparazione_del_gioco;
		preparazione_del_gioco = "Preparazione del gioco \nCercare nella pila delle tessere la tessera iniziale e posizionarla scoperta al centro dell'area di gioco. Mischiare le restanti tessere e tenerle coperte. \nOgni giocatore avrà a disposizione davanti a sé i 7 segnalini di controllo per utilizzarli durante la partita. \nSelezionare il giocatore che inizierà per primo a giocare. \n\n";
		String svolgimento_del_gioco_intro;
		svolgimento_del_gioco_intro = "Svolgimento del gioco \nIl gioco procede a turni, a partire dal primo giocatore, si procederà in senso orario. Ad ogni turno, il giocatore attivo compie le seguenti azioni in questo preciso ordine: \n1) pesca una tessera territorio coperta e la posizione nell'area di gioco; \n2) opzionalmente, posiziona uno dei suoi segnalini di controllo sulla tessera appena messa in gioco; \n3) se il posizionamento della tessera ha completato strade o città, sono conteggiati i punti vittoria corrispondenti e i segnalini di controllo posizionati su di esse saranno nuovamente disponibili. \n ";
		String svolgimento_del_gioco_Azione1;
		svolgimento_del_gioco_Azione1 = "Azione 1: Posizionamento delle tessere \nPescare una tessera, mostrarla a tutti i giocatori, quindi metterla nell'area del gioco attenendosi alle seguenti regole: \n-la nuova tessera deve avere almeno un lato in contatto con il lato di una delle tessere già in gioco; \n-la nuova tessera va posizionata in modo che città, strade e campi siano contigui a quelli della/e tessera/e con cui è in contatto (ad esempio una strada non può finire in un campo, oppure una città non può attaccarsi ad un campo senza chiudere le mura). \nSe per un qualche motivo la tessera pescata non avesse possibili posizionamenti legali questa è scartata e sostituita con un'altra. \n";
		String svolgimento_del_gioco_Azione2;
		svolgimento_del_gioco_Azione2 = "Azione 2: Posizionamento dei segnalini di controllo \nQuando un giocatore ha posizionato una tessera può, se lo desidera, posizionare uno dei suoi segnalini di controllo attenendosi alle seguenti regole: \n-si può posizionare un solo segnalino per turno; \n-il segnalino posizionato deve essere uno di quelli disponibili (quindi non può essere recuperato da quelli già posizionati sulle tessere in gioco); \n-può essere posizionato esclusivamente sulla tessera che è stata appena messa in gioco; \n-̀ necessario scegliere su quale parte specifica della tessera posizionarlo (cioè va posizionato sopra la porzione di strada o città che deve essere controllata); \n-non si può posizionare il segnalino su una strada/città se altre porzioni della stessa, appartenenti a tessere precedentemente posizionate, contengono già un segnalino (è comunque possibile che, in seguito al posizionamento di una tessera, due città/strade inizialmente separate e dotate di segnalino si uniscano a formarne una sola). \nNel caso in cui un giocatore non avesse più segnalini di controllo disponibili, continuerà a giocare le tessere normalmente, ma non potrà posizionarvi sopra alcunché finché non avrà recuperato dei segnalini di controllo. \n";
		String svolgimento_del_gioco_Azione3;
		svolgimento_del_gioco_Azione3 = "Azione 3: Conteggio dei punti vittoria \n Ogni qual volta delle strade o delle città vengono completate in seguito al posizionamento di una tessera, esse forniscono immediatamente dei punti vittoria al giocatore che le controlla. Un giocatore controlla una strada o una città quando possiede la maggioranza di segnalini di controllo in quella strada o città. Nel caso in cui più giocatori si contendano una strada o città con lo stesso numero di segnalini, i punti vittoria sono attribuiti per intero a tutti i giocatori contendenti. \nDopo l'attribuzione dei punti vittoria relativi al completamento di una strada o di una città, i segnalini che vi erano posizionati sopra saranno nuovamente disponibili. \nPunteggio strada \nUna strada si considera completa quando ambedue le estremità terminano in un incrocio, una città o se la strada compie un cerchio completo su di essa. Non c'è limite al numero di tratti di cui la strada è composta. Il giocatore che controlla una strada completa ottiene il relativo punteggio pari ad 1 punto per ogni tessera di strada (se la strada dovesse passare due volte nella stessa tessera questa varrebbe solo 1 punto). Nel caso in cui alla fine del gioco la strada restasse incompleta, questa farebbe guadagnare al giocatore che la controlla comunque 1 punto per ogni tessera. \nPunteggio città \nUna città si considera completa quando è interamente circondata da mura senza lo spazio per ulteriori tessere all’interno delle mura. Non c'è alcun limite al numero di tessere di cui è composta la città. Il giocatore che controlla la città completa totalizza un punteggio di 2 punti per ogni tessera che compone la città. Nel caso in cui alla fine del gioco la città restasse incompleta, questa farebbe guadagnare al giocatore che la controlla comunque 1 punto per ogni tessera. \nRecupero dei segnalini di controllo \nOgni qual volta venga completata una strada o una città, dopo aver assegnato eventuali punti vittoria, i segnalini di controllo posizionati sull'elemento completato verranno nuovamente resi disponibili ai rispettivi giocatori. È quindi possibile per un giocatore posizionare una tessera che completa qualcosa, metterci sopra un segnalino, totalizzare i punti ed avere il segnalino disponibile, tutto nello stesso turno. \n\n";
		String fine_del_gioco;
		fine_del_gioco = "Non appena viene posizionata l'ultima tessera il gioco termina e si procede con il conteggio dei punti relativi alle strade e città non completate, i quali si sommeranno ai punti guadagnati durante la partita. \n\n";
		
		String unified = intro + materiale_a_disposizione + scopo_del_gioco + preparazione_del_gioco + svolgimento_del_gioco_intro + svolgimento_del_gioco_Azione1 + svolgimento_del_gioco_Azione2 + svolgimento_del_gioco_Azione3 + fine_del_gioco;
		setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea (unified);
		textArea.setFont(new Font("Papyrus", Font.PLAIN, 13));

		textArea.setFocusable (false);
		textArea.setEditable (false);
		textArea.setLineWrap (true);
		textArea.setWrapStyleWord (true);

		JScrollPane scrollPane = new JScrollPane (textArea,
		                                          JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                                          JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add (scrollPane);
	}

}
