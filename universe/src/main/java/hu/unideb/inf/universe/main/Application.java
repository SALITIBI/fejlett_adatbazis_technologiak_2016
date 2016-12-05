package hu.unideb.inf.universe.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;

import hu.unideb.inf.universe.connection.ConnectionUtil;
import hu.unideb.inf.universe.exception.UniverseException;
import hu.unideb.inf.universe.model.Comet;
import hu.unideb.inf.universe.model.Galaxy;
import hu.unideb.inf.universe.model.Mineral;
import hu.unideb.inf.universe.model.Moon;
import hu.unideb.inf.universe.model.Planet;
import hu.unideb.inf.universe.model.SolarSystem;
import hu.unideb.inf.universe.model.Star;
import hu.unideb.inf.universe.service.UniverseService;
import hu.unideb.inf.universe.service.UniverseServiceImpl;

public class Application {

	private static JComboBox<Galaxy> galaxiesComboBox;
	private static JComboBox<SolarSystem> solarSystemsComboBox;
	private static JComboBox<Star> starComboBox;
	private static JComboBox<Planet> planetsComboBox;
	private static JComboBox<Moon> moonsComboBox;
	private static JComboBox<Comet> cometsComboBox;
	private static JComboBox<Mineral> mineralsComboBox;

	public static void main(String[] args) throws Exception {
		XQConnection xqc = ConnectionUtil.getConnection("localhost", "1984", "admin", "admin");
		UniverseService us = new UniverseServiceImpl(xqc, "universe");

		JFrame frame = new JFrame("Fejlett XML Technológiák beadandó 2016");

		JMenu actionsMenu = new JMenu("Actions");
		actionsMenu.setEnabled(false);
		// actionsMenu.add(new JMenuItem("Add new galaxy"));
		// actionsMenu.add(new JMenuItem("Add new solar system"));
		// actionsMenu.add(new JMenuItem("Add new planet"));
		// actionsMenu.add(new JMenuItem("Add new moon"));
		// actionsMenu.add(new JMenuItem("Add new comet"));
		// actionsMenu.add(new JMenuItem("Add new mineral"));

		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(e -> {
			JPanel dialogPanel = new JPanel();
			dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
			dialogPanel.add(new JLabel("Czuczor Gergő"));
			dialogPanel.add(new JLabel("Kiss Sándor Ádám"));
			dialogPanel.add(new JLabel("Salagvárdi Tibor"));
			dialogPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

			JDialog dialog = new JDialog(frame, "About us", true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.add(dialogPanel);
			dialog.pack();
			dialog.setVisible(true);
		});

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(aboutMenuItem);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(actionsMenu);
		menuBar.add(helpMenu);

		JPanel contentListerPanel = new JPanel();
		contentListerPanel.setLayout(new BoxLayout(contentListerPanel, BoxLayout.Y_AXIS));

		Galaxy[] galaxies = us.findAllGalaxies().toArray(new Galaxy[0]);
		SolarSystem[] solarSystems = us.findAllSolarSystemsInGalaxy(galaxies[0]).toArray(new SolarSystem[0]);
		Star star = us.findStarInSolarSystem(solarSystems[0]);
		Planet[] planets = us.findAllPlanetsInSolarSystem(solarSystems[0]).toArray(new Planet[0]);
		Moon[] moons = us.findAllMoonsAroundPlanet(planets[0]).toArray(new Moon[0]);
		Comet[] comets = us.findAllCometsInSolarSystem(solarSystems[0]).toArray(new Comet[0]);
		Mineral[] minerals = us.findAllMineralsInComet(comets[0]).toArray(new Mineral[0]);

		galaxiesComboBox = new JComboBox<>(galaxies);
		galaxiesComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		// galaxiesComboBox.setRenderer(new ListCellRenderer<Galaxy>() {
		// @Override
		// public Component getListCellRendererComponent(JList<? extends Galaxy> list, Galaxy value, int index,
		// boolean isSelected, boolean cellHasFocus) {
		// // TODO csak a galaxis neve kellene, hogy megjelenjen!
		// return null;
		// }
		// });
		galaxiesComboBox.addActionListener(e -> {
			updateSolarSystemsComboBox(us);
			updateStarComboBox(us);
			updatePlanetsComboBox(us);
			updateCometsComboBox(us);
			updateMoonsComboBox(us);
			updateMineralsComboBox(us);
		});

		Dimension buttonPreferredSize = new Dimension(180, 25);

		JButton addGalaxyButton = new JButton("Add galaxy");
		addGalaxyButton.setPreferredSize(buttonPreferredSize);
		JButton updateGalaxyButton = new JButton("Update galaxy");
		updateGalaxyButton.setPreferredSize(buttonPreferredSize);
		JButton deleteGalaxyButton = new JButton("Delete galaxy");
		deleteGalaxyButton.setPreferredSize(buttonPreferredSize);

		Dimension buttonPanelPreferredSize = new Dimension(200, 30 * 3);

		JPanel galaxiesButtonPanel = new JPanel(new FlowLayout());
		galaxiesButtonPanel.setPreferredSize(buttonPanelPreferredSize);
		galaxiesButtonPanel.add(addGalaxyButton);
		galaxiesButtonPanel.add(updateGalaxyButton);
		galaxiesButtonPanel.add(deleteGalaxyButton);

		JPanel galaxiesRightPanel = new JPanel();
		galaxiesRightPanel.setLayout(new BoxLayout(galaxiesRightPanel, BoxLayout.Y_AXIS));
		galaxiesRightPanel.add(Box.createGlue());
		galaxiesRightPanel.add(galaxiesButtonPanel);
		galaxiesRightPanel.add(Box.createGlue());

		JPanel galaxiesPanel = new JPanel(new BorderLayout());
		galaxiesPanel.add(galaxiesComboBox, BorderLayout.CENTER);
		galaxiesPanel.add(galaxiesRightPanel, BorderLayout.EAST);
		contentListerPanel.add(galaxiesPanel);

		solarSystemsComboBox = new JComboBox<>(solarSystems);
		solarSystemsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		solarSystemsComboBox.addActionListener(e -> {
			updateStarComboBox(us);
			updatePlanetsComboBox(us);
			updateCometsComboBox(us);
			updateMoonsComboBox(us);
			updateMineralsComboBox(us);
		});

		JButton addSolarSystemButton = new JButton("Add solar system");
		addSolarSystemButton.setPreferredSize(buttonPreferredSize);
		JButton updateSolarSystemButton = new JButton("Update solar system");
		updateSolarSystemButton.setPreferredSize(buttonPreferredSize);
		JButton deleteSolarSystemButton = new JButton("Delete solar system");
		deleteSolarSystemButton.setPreferredSize(buttonPreferredSize);

		JPanel solarSystemsButtonPanel = new JPanel(new FlowLayout());
		solarSystemsButtonPanel.setPreferredSize(buttonPanelPreferredSize);
		solarSystemsButtonPanel.add(addSolarSystemButton);
		solarSystemsButtonPanel.add(updateSolarSystemButton);
		solarSystemsButtonPanel.add(deleteSolarSystemButton);

		JPanel solarSystemsRightPanel = new JPanel();
		solarSystemsRightPanel.setLayout(new BoxLayout(solarSystemsRightPanel, BoxLayout.Y_AXIS));
		solarSystemsRightPanel.add(Box.createGlue());
		solarSystemsRightPanel.add(solarSystemsButtonPanel);
		solarSystemsRightPanel.add(Box.createGlue());

		JPanel solarSystemsPanel = new JPanel(new BorderLayout());
		solarSystemsPanel.add(solarSystemsComboBox, BorderLayout.CENTER);
		solarSystemsPanel.add(solarSystemsRightPanel, BorderLayout.EAST);
		contentListerPanel.add(solarSystemsPanel);

		starComboBox = new JComboBox<>(new Star[] { star });
		starComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentListerPanel.add(starComboBox);

		planetsComboBox = new JComboBox<>(planets);
		planetsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		planetsComboBox.addActionListener(e -> {
			updateMoonsComboBox(us);
		});

		JButton addPlanetButton = new JButton("Add planet");
		addPlanetButton.setPreferredSize(buttonPreferredSize);
		JButton updatePlanetButton = new JButton("Update planet");
		updatePlanetButton.setPreferredSize(buttonPreferredSize);
		JButton deletePlanetButton = new JButton("Delete planet");
		deletePlanetButton.setPreferredSize(buttonPreferredSize);

		JPanel planetsButtonPanel = new JPanel(new FlowLayout());
		planetsButtonPanel.setPreferredSize(buttonPanelPreferredSize);
		planetsButtonPanel.add(addPlanetButton);
		planetsButtonPanel.add(updatePlanetButton);
		planetsButtonPanel.add(deletePlanetButton);

		JPanel planetsRightPanel = new JPanel();
		planetsRightPanel.setLayout(new BoxLayout(planetsRightPanel, BoxLayout.Y_AXIS));
		planetsRightPanel.add(Box.createGlue());
		planetsRightPanel.add(planetsButtonPanel);
		planetsRightPanel.add(Box.createGlue());

		JPanel planetsPanel = new JPanel(new BorderLayout());
		planetsPanel.add(planetsComboBox, BorderLayout.CENTER);
		planetsPanel.add(planetsRightPanel, BorderLayout.EAST);
		contentListerPanel.add(planetsPanel);

		moonsComboBox = new JComboBox<>(moons);
		moonsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton addMoonButton = new JButton("Add moon");
		addMoonButton.setPreferredSize(buttonPreferredSize);
		JButton updateMoonButton = new JButton("Update moon");
		updateMoonButton.setPreferredSize(buttonPreferredSize);
		JButton deleteMoonButton = new JButton("Delete moon");
		deleteMoonButton.setPreferredSize(buttonPreferredSize);

		JPanel moonsButtonPanel = new JPanel(new FlowLayout());
		moonsButtonPanel.setPreferredSize(buttonPanelPreferredSize);
		moonsButtonPanel.add(addMoonButton);
		moonsButtonPanel.add(updateMoonButton);
		moonsButtonPanel.add(deleteMoonButton);

		JPanel moonsRightPanel = new JPanel();
		moonsRightPanel.setLayout(new BoxLayout(moonsRightPanel, BoxLayout.Y_AXIS));
		moonsRightPanel.add(Box.createGlue());
		moonsRightPanel.add(moonsButtonPanel);
		moonsRightPanel.add(Box.createGlue());

		JPanel moonsPanel = new JPanel(new BorderLayout());
		moonsPanel.add(moonsComboBox, BorderLayout.CENTER);
		moonsPanel.add(moonsRightPanel, BorderLayout.EAST);
		contentListerPanel.add(moonsPanel);

		cometsComboBox = new JComboBox<>(comets);
		cometsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		cometsComboBox.addActionListener(e -> {
			updateMineralsComboBox(us);
		});

		JButton addCometButton = new JButton("Add comet");
		addCometButton.setPreferredSize(buttonPreferredSize);
		JButton updateCometButton = new JButton("Update comet");
		updateCometButton.setPreferredSize(buttonPreferredSize);
		JButton deleteCometButton = new JButton("Delete comet");
		deleteCometButton.setPreferredSize(buttonPreferredSize);

		JPanel cometsButtonPanel = new JPanel(new FlowLayout());
		cometsButtonPanel.setPreferredSize(buttonPanelPreferredSize);
		cometsButtonPanel.add(addCometButton);
		cometsButtonPanel.add(updateCometButton);
		cometsButtonPanel.add(deleteCometButton);

		JPanel cometsRightPanel = new JPanel();
		cometsRightPanel.setLayout(new BoxLayout(cometsRightPanel, BoxLayout.Y_AXIS));
		cometsRightPanel.add(Box.createGlue());
		cometsRightPanel.add(cometsButtonPanel);
		cometsRightPanel.add(Box.createGlue());

		JPanel cometsPanel = new JPanel(new BorderLayout());
		cometsPanel.add(cometsComboBox, BorderLayout.CENTER);
		cometsPanel.add(cometsRightPanel, BorderLayout.EAST);
		contentListerPanel.add(cometsPanel);

		mineralsComboBox = new JComboBox<>(minerals);
		mineralsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton addMineralButton = new JButton("Add mineral");
		addMineralButton.setPreferredSize(buttonPreferredSize);
		JButton updateMineralButton = new JButton("Update mineral");
		updateMineralButton.setPreferredSize(buttonPreferredSize);
		JButton deleteMineralButton = new JButton("Delete mineral");
		deleteMineralButton.setPreferredSize(buttonPreferredSize);

		JPanel mineralsButtonPanel = new JPanel(new FlowLayout());
		mineralsButtonPanel.setPreferredSize(buttonPanelPreferredSize);
		mineralsButtonPanel.add(addMineralButton);
		mineralsButtonPanel.add(updateMineralButton);
		mineralsButtonPanel.add(deleteMineralButton);

		JPanel mineralsRightPanel = new JPanel();
		mineralsRightPanel.setLayout(new BoxLayout(mineralsRightPanel, BoxLayout.Y_AXIS));
		mineralsRightPanel.add(Box.createGlue());
		mineralsRightPanel.add(mineralsButtonPanel);
		mineralsRightPanel.add(Box.createGlue());

		JPanel mineralsPanel = new JPanel(new BorderLayout());
		mineralsPanel.add(mineralsComboBox, BorderLayout.CENTER);
		mineralsPanel.add(mineralsRightPanel, BorderLayout.EAST);
		contentListerPanel.add(mineralsPanel);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(menuBar, BorderLayout.NORTH);
		panel.add(contentListerPanel, BorderLayout.CENTER);

		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMaximumSize(new Dimension(1280, 720));
		frame.setPreferredSize(new Dimension(1280, 720));
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					if (!xqc.isClosed()) {
						xqc.close();
					}
				} catch (XQException ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
	}

	public static void updateGalaxiesComboBox(UniverseService us) {
		try {
			List<Galaxy> galaxies = us.findAllGalaxies();
			galaxiesComboBox.setModel(new DefaultComboBoxModel<>(galaxies.toArray(new Galaxy[0])));
		} catch (UniverseException e) {
			e.printStackTrace();
		}
	}

	public static void updateSolarSystemsComboBox(UniverseService us) {
		try {
			Object selectedGalaxy = galaxiesComboBox.getSelectedItem();
			if (selectedGalaxy instanceof Galaxy) {
				List<SolarSystem> solarSystems = us.findAllSolarSystemsInGalaxy((Galaxy) selectedGalaxy);
				solarSystemsComboBox.setModel(new DefaultComboBoxModel<>(solarSystems.toArray(new SolarSystem[0])));
			} else {
				solarSystemsComboBox.setModel(new DefaultComboBoxModel<>());
			}
		} catch (UniverseException e) {
			e.printStackTrace();
		}
	}

	public static void updateStarComboBox(UniverseService us) {
		try {
			Object selectedSolarSystem = solarSystemsComboBox.getModel().getSelectedItem();
			if (selectedSolarSystem instanceof SolarSystem) {
				Star star = us.findStarInSolarSystem((SolarSystem) selectedSolarSystem);
				starComboBox.setModel(new DefaultComboBoxModel<>(new Star[] { star }));
			} else {
				starComboBox.setModel(new DefaultComboBoxModel<>());
			}
		} catch (UniverseException e) {
			e.printStackTrace();
		}
	}

	public static void updatePlanetsComboBox(UniverseService us) {
		try {
			Object selectedSolarSystem = solarSystemsComboBox.getModel().getSelectedItem();
			if (selectedSolarSystem instanceof SolarSystem) {
				List<Planet> planets = us.findAllPlanetsInSolarSystem((SolarSystem) selectedSolarSystem);
				planetsComboBox.setModel(new DefaultComboBoxModel<>(planets.toArray(new Planet[0])));
			} else {
				planetsComboBox.setModel(new DefaultComboBoxModel<>());
			}
		} catch (UniverseException e) {
			e.printStackTrace();
		}
	}

	public static void updateCometsComboBox(UniverseService us) {
		try {
			Object selectedSolarSystem = solarSystemsComboBox.getModel().getSelectedItem();
			if (selectedSolarSystem instanceof SolarSystem) {
				List<Comet> comets = us.findAllCometsInSolarSystem((SolarSystem) selectedSolarSystem);
				cometsComboBox.setModel(new DefaultComboBoxModel<>(comets.toArray(new Comet[0])));
			} else {
				cometsComboBox.setModel(new DefaultComboBoxModel<>());
			}
		} catch (UniverseException e) {
			e.printStackTrace();
		}
	}

	public static void updateMoonsComboBox(UniverseService us) {
		try {
			Object selectedPlanet = planetsComboBox.getSelectedItem();
			if (selectedPlanet instanceof Planet) {
				List<Moon> moons = us.findAllMoonsAroundPlanet((Planet) selectedPlanet);
				moonsComboBox.setModel(new DefaultComboBoxModel<>(moons.toArray(new Moon[0])));
			} else {
				moonsComboBox.setModel(new DefaultComboBoxModel<>());
			}
		} catch (UniverseException e) {
			e.printStackTrace();
		}
	}

	public static void updateMineralsComboBox(UniverseService us) {
		try {
			Object selectedComet = cometsComboBox.getSelectedItem();
			if (selectedComet instanceof Comet) {
				List<Mineral> minerals = us.findAllMineralsInComet((Comet) selectedComet);
				mineralsComboBox.setModel(new DefaultComboBoxModel<>(minerals.toArray(new Mineral[0])));
			} else {
				mineralsComboBox.setModel(new DefaultComboBoxModel<>());
			}
		} catch (UniverseException e) {
			e.printStackTrace();
		}
	}

}
