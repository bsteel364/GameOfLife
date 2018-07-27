import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.Random;

public class SimpleTableDemo extends JPanel implements ChangeListener {
	Board board = null;
    private boolean DEBUG = false;
    private Object[][] data;
    private JTable table = null;
    private DefaultTableModel model = null;
    private int gridWidth; // The number of rows in grid (same as columns)
    private static SimpleTableDemo instance = null;
    private int sleepTime = 200;
    private Thread runThread;
    private JProgressBar progressBar = null;


    // Since this class is a Singleton, don't allow instantiation through the constructor
    private SimpleTableDemo() {
        super(new BorderLayout());
        init();
    }

    // Method for obtaining singleton instance. This method MUST be static.
    public static SimpleTableDemo getInstance() {
        if(instance == null) // Instance will only be null the first time this method is called.
            instance = new SimpleTableDemo(); // We create the one and only instance the first time through only
        return instance;
    }

    public void init() {
    	board = new Board(100);
    	gridWidth = board.getWidth();
    	
        String[] columnNames = new String[gridWidth];
        for(int i = 0; i < gridWidth; i++) {
            columnNames[i] = "";
        }
        
        board.randomlyFillBoard(15, 3);
        data = board.getIntegerMatrix();
        new Random();

        /*
        for(int j = 0; j < gridWidth; j++) {
            for(int i = 0; i < gridWidth; i++) {
//                data[i][j] = random.nextInt(2);
                data[i][j] = 0;
            }
        }
		*/
        
        model = new DefaultTableModel(data, columnNames){
                @Override
                public Class getColumnClass(int columnIndex) {
                    return java.lang.Integer.class;
                }
        };

        table = new JTable(model){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int columnIndex) {
                JComponent component = (JComponent) super.prepareRenderer(renderer, rowIndex, columnIndex);

                if(((Integer)getValueAt(rowIndex, columnIndex)).equals(Integer.valueOf(0))) {
                    component.setBackground(Color.BLACK);
                    component.setForeground(Color.BLACK); // THIS WILL HIDE THE VALUE
                } else if(((Integer)getValueAt(rowIndex, columnIndex)).equals(Integer.valueOf(2))) {
                	component.setBackground(Color.RED);
                    component.setForeground(Color.RED); // THIS WILL HIDE THE VALUE
                }
                else {
                    component.setBackground(Color.GREEN);
                    component.setForeground(Color.GREEN); // THIS WILL HIDE THE VALUE
                }

                return component;
            }
        };

        table.setPreferredScrollableViewportSize(new Dimension(20*gridWidth, 16*gridWidth)); //Scale size to grid size
        table.setFillsViewportHeight(true);

        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }
        JMenuBar bar = new JMenuBar();

        JMenu menu = new JMenu("File");

        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                SimpleTableDemo instance = SimpleTableDemo.getInstance();
                instance.stop();
                instance.restart();
            }
          });
        menu.add(restart);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
              System.exit(0);
            }
          });
        menu.add(exit);

        bar.add(menu);
        add(bar, BorderLayout.NORTH);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.CENTER);
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 250);

        slider.setMinorTickSpacing(10);
        slider.setMajorTickSpacing(200);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        Hashtable labelTable = new Hashtable();
        labelTable.put( new Integer( 0 ), new JLabel("Fast") );
        labelTable.put( new Integer(500), new JLabel("Medium") );
        labelTable.put( new Integer(1000), new JLabel("Slow") );
        slider.setLabelTable( labelTable );

        slider.addChangeListener(this);

        progressBar = new JProgressBar(0, 999); //In the run for loop we go to < 1000 so 999 will be 100%
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBackground(Color.BLACK);
        progressBar.setForeground(Color.GREEN);
        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected Color getSelectionBackground() { return Color.WHITE; }
            @Override
            protected Color getSelectionForeground() { return Color.BLACK; }
          });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(slider, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);
        bottomPanel.setPreferredSize(new Dimension(200, 100));
        add(bottomPanel, BorderLayout.SOUTH);

    }

    /**
     *
     */
    protected void restart() {
        System.out.println("Restart called.");
        for(int j = 0; j < gridWidth; j++) {
            for(int i = 0; i < gridWidth; i++) {
//                data[i][j] = random.nextInt(2);
                model.setValueAt(0, i, j);
            }
        }
        Runnable r = new Runnable() {
            public void run() {
                instance.run();
            }};
        Thread t = new Thread(r);
        t.start();
    }

    /**
     *
     */
    protected void stop() {
        System.out.println("Stop called.");
        runThread.interrupt();
    }

    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SimpleTableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        SimpleTableDemo newContentPane = getInstance();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void run() {
        this.progressBar.setValue(0);
        runThread = Thread.currentThread();
        model.fireTableDataChanged();
        for(int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(sleepTime); // Update every 2/10 of a second
            }
            catch (InterruptedException e) {
                System.out.println("Interrupting run");
                return;
            }
            board.update();
            data= board.getIntegerMatrix();
            model.fireTableDataChanged();
            for(int j = 0; j < gridWidth; j++) {
            	for(int k = 0; k < gridWidth; k++) {
            		 table.setValueAt(data[j][k], j, k);
            	}
            }
            this.progressBar.setValue(i);
//            System.out.println("update");
        }
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

        //Have to wait half a sec while table is built, otherwise NPE in run when we set the table value
        try {
            Thread.sleep(500); // Update every 2/10 of a second
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        SimpleTableDemo.getInstance().run();
    }

    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            this.sleepTime = source.getValue();
        }

    }
}
