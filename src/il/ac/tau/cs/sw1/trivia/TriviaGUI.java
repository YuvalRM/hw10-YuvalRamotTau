package il.ac.tau.cs.sw1.trivia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TriviaGUI {
	

	private static final int MAX_ERRORS = 3;
	private Shell shell;
	private Label scoreLabel;
	private Composite questionPanel;
	private Label startupMessageLabel;
	private Font boldFont;
	private String lastAnswer;
	
	// Currently visible UI elements.
	Label instructionLabel;
	Label questionLabel;
	private List<Button> answerButtons = new LinkedList<>();
	private Button passButton;
	private Button fiftyFiftyButton;
	private int score;
	private int mis;
	private int index;
	private boolean finished;
	private Question curr;
	private Iterator <Question> qs;
	private int count5050;
	private int countpass;
	
	private final String WA="Wrong...";
	private final String RA="Correct";
	private final String GAMEOVER="GAME OVER";
	
	//my adds
	private List<Question> questions;
	public class Question{
		private String q;
		private String rA;
		private List<String> wA= new ArrayList<String>();
		private Set<String> ans = new HashSet<String>();
		public Question(String[] q) {
			this.q=q[0];
			this.rA=q[1];
			this.wA.add(q[1]);
			this.wA.add(q[2]);
			this.wA.add(q[3]);
			this.wA.add(q[4]);
			ans.addAll(wA);
			Collections.shuffle(wA);
		}
		public String toString() {
			return q+":"+rA+","+wA.toString();
		}
		public boolean equals(Object obj) {
			if (this == obj) {
				 return true;}
				 if (obj == null) {
				 return false;}
				 if (getClass() != obj.getClass()) {
				 return false;}
				 Question other = (Question) obj;
				 if (this.q!=other.q) {
				     return false;
				     
				 }
				 if(!(other.wA.equals(this.wA))) {
			    	 return false;
			     }
				 return true;
		}
	}

	public void open() {
		createShell();
		runApplication();
	}

	/**
	 * Creates the widgets of the application main window
	 */
	private void createShell() {
		Display display = Display.getDefault();
		shell = new Shell(display);
		shell.setText("Trivia");

		// window style
		Rectangle monitor_bounds = shell.getMonitor().getBounds();
		shell.setSize(new Point(monitor_bounds.width / 3,
				monitor_bounds.height / 4));
		shell.setLayout(new GridLayout());

		FontData fontData = new FontData();
		fontData.setStyle(SWT.BOLD);
		boldFont = new Font(shell.getDisplay(), fontData);

		// create window panels
		createFileLoadingPanel();
		createScorePanel();
		createQuestionPanel();
	}

	/**
	 * Creates the widgets of the form for trivia file selection
	 */
	private void createFileLoadingPanel() {
		final Composite fileSelection = new Composite(shell, SWT.NULL);
		fileSelection.setLayoutData(GUIUtils.createFillGridData(1));
		fileSelection.setLayout(new GridLayout(4, false));

		final Label label = new Label(fileSelection, SWT.NONE);
		label.setText("Enter trivia file path: ");

		// text field to enter the file path
		final Text filePathField = new Text(fileSelection, SWT.SINGLE
				| SWT.BORDER);
		filePathField.setLayoutData(GUIUtils.createFillGridData(1));

		// "Browse" button
		final Button browseButton = new Button(fileSelection, SWT.PUSH);
		browseButton.setText("Browse");
		browseButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String path =GUIUtils.getFilePathFromFileDialog(shell);
				if(path!=null) {
				filePathField.setText(path);
			}
			
			}});

		// "Play!" button
		final Button playButton = new Button(fileSelection, SWT.PUSH);
		playButton.setText("Play!");
		playButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String fileName=filePathField.getText();
				File fromFile=new File(fileName);
				questions=new LinkedList<Question>();
				try {
					@SuppressWarnings("resource")
					BufferedReader bufferedReader = new BufferedReader(new FileReader(fromFile));
					String word=null;
					while ((word=bufferedReader.readLine())!=null) {
						String [] words=word.split("	");
						questions.add(new Question(words));
						
					}
					count5050=0;
					countpass=0;
					Collections.shuffle(questions);
					score=0;
					mis=0;
					index=0;
					qs=questions.iterator();
					curr=qs.next();
					updateQuestionPanel( curr.q,curr.wA);
					scoreLabel.setText("0");
					finished=false;
					
				} catch (IOException e) {
					GUIUtils.showErrorDialog(shell,"An Error Accured");
				}
				
				
			}
			
		});
	}

	/**
	 * Creates the panel that displays the current score
	 */
	private void createScorePanel() {
		Composite scorePanel = new Composite(shell, SWT.BORDER);
		scorePanel.setLayoutData(GUIUtils.createFillGridData(1));
		scorePanel.setLayout(new GridLayout(2, false));

		final Label label = new Label(scorePanel, SWT.NONE);
		label.setText("Total score: ");

		// The label which displays the score; initially empty
		scoreLabel = new Label(scorePanel, SWT.NONE);
		scoreLabel.setLayoutData(GUIUtils.createFillGridData(1));
	}

	/**
	 * Creates the panel that displays the questions, as soon as the game
	 * starts. See the updateQuestionPanel for creating the question and answer
	 * buttons
	 */
	private void createQuestionPanel() {
		questionPanel = new Composite(shell, SWT.BORDER);
		questionPanel.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));
		questionPanel.setLayout(new GridLayout(2, true));

		// Initially, only displays a message
		startupMessageLabel = new Label(questionPanel, SWT.NONE);
		startupMessageLabel.setText("No question to display, yet.");
		startupMessageLabel.setLayoutData(GUIUtils.createFillGridData(2));
	}

	/**
	 * Serves to display the question and answer buttons
	 */
	private void updateQuestionPanel(String question, List<String> answers) {
		// Save current list of answers.
		List<String> currentAnswers = answers;
		
		// clear the question panel
		Control[] children = questionPanel.getChildren();
		for (Control control : children) {
			control.dispose();
		}

		// create the instruction label
		instructionLabel = new Label(questionPanel, SWT.CENTER | SWT.WRAP);
		instructionLabel.setText(lastAnswer + "Answer the following question:");
		instructionLabel.setLayoutData(GUIUtils.createFillGridData(2));

		// create the question label
		questionLabel = new Label(questionPanel, SWT.CENTER | SWT.WRAP);
		questionLabel.setText(question);
		questionLabel.setFont(boldFont);
		questionLabel.setLayoutData(GUIUtils.createFillGridData(2));

		// create the answer buttons
		answerButtons.clear();
		for (int i = 0; i < 4; i++) {
			Button answerButton = new Button(questionPanel, SWT.PUSH | SWT.WRAP);
			answerButton.setText(answers.get(i));
			GridData answerLayoutData = GUIUtils.createFillGridData(1);
			answerLayoutData.verticalAlignment = SWT.FILL;
			answerButton.setLayoutData(answerLayoutData);
			answerButton.addSelectionListener(new SelectionListener() {
				boolean right=curr.rA==answerButton.getText();

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void widgetSelected(SelectionEvent arg0) {
					if(!finished) {
						
					if(right) {
						score+=3;
						mis=0;
						lastAnswer=RA;
					}
					else {
						mis+=1;
						lastAnswer=WA;
						score-=2;
						if(mis==MAX_ERRORS) {
							finished=true;
						}
					}
					index+=1;
					if(!qs.hasNext()) {
						finished=true;
					}
					scoreLabel.setText(Integer.toString(score));
					if(!finished) {
					curr=qs.next();
					updateQuestionPanel(curr.q, curr.wA);
					}
					else {
						GUIUtils.showInfoDialog(shell, GAMEOVER, "Your final score is "+score+" after "+(index)+" questions.");
					}
					}

					
				}
				
			});
			answerButtons.add(answerButton);
		}

		// create the "Pass" button to skip a question
		passButton = new Button(questionPanel, SWT.PUSH);
		passButton.setText("Pass");
		GridData data = new GridData(GridData.END, GridData.CENTER, true,
				false);
		data.horizontalSpan = 1;
		passButton.setLayoutData(data);
		passButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(!finished) {
					score--;
					countpass++;

					scoreLabel.setText(Integer.toString(score));
					if(qs.hasNext()) {
						curr=qs.next();
						updateQuestionPanel(curr.q, curr.wA);
					}
					if(!qs.hasNext()) {
						GUIUtils.showInfoDialog(shell, GAMEOVER, "Your final score is "+score+" after "+(index)+" questions.");
					}
				}
				
			}
			
		});
		
		// create the "50-50" button to show fewer answer options
		fiftyFiftyButton = new Button(questionPanel, SWT.PUSH);
		
		if(countpass!=0&&score<=0) {
			passButton.setEnabled(false);
		}
		else {
			passButton.setEnabled(true);
		}
		if(count5050!=0&&score<=0) {
			fiftyFiftyButton.setEnabled(false);
		}
		else {
			fiftyFiftyButton.setEnabled(true);
		}
		fiftyFiftyButton.setText("50-50");
		fiftyFiftyButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(!finished) {
						count5050++;
						score-=1;
						int disabled=0;
						Collections.shuffle(answerButtons);
						for(int j=0; j<4&&disabled<2;j++) {
							Button b=answerButtons.get(j);
								if(b.getText()==curr.rA) {
									continue;

						       	}
								else {
									b.setEnabled(false);
									disabled++;
								}
							fiftyFiftyButton.setEnabled(false);
							
						}
						
					}
					scoreLabel.setText(Integer.toString(score));
					
				}
				
			
			
		});
		data = new GridData(GridData.BEGINNING, GridData.CENTER, true,
				false);
		data.horizontalSpan = 1;
		fiftyFiftyButton.setLayoutData(data);

		// two operations to make the new widgets display properly
		questionPanel.pack();
		questionPanel.getParent().layout();
	}

	/**
	 * Opens the main window and executes the event loop of the application
	 */
	private void runApplication() {
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		boldFont.dispose();
	}
}
