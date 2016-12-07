package hu.unideb.inf.universe.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
import hu.unideb.inf.universe.model.Property;
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
		if (!us.validate()) {
			System.exit(-1);
		}

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
		galaxiesComboBox.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Galaxy) {
					setText(((Galaxy) value).getName());
				}
				return component;
			}
		});
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
		addGalaxyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String galaxyName = (String) JOptionPane.showInputDialog(frame, "Name for the new galaxy:\n", "Add a new galaxy",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (galaxyName != null && !"".equals(galaxyName)) {
					try {
						us.addGalaxyToUniverse(galaxyName);
						updateGalaxiesComboBox(us);
					} catch (UniverseException e1) {
						JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
					}
				} else if ("".equals(galaxyName)) {
					JOptionPane.showMessageDialog(frame, "The galaxy's name cannot be empty!", "Failed to create Galaxy",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		deleteGalaxyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					us.deleteGalaxy(((Galaxy) galaxiesComboBox.getSelectedItem()).getName());
				} catch (UniverseException e1) {
					JOptionPane.showMessageDialog(frame, "The galaxy's name cannot be empty!", "Failed to create Galaxy",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

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
		galaxiesPanel.add(new JLabel("Galaxy: "), BorderLayout.NORTH);
		galaxiesPanel.add(galaxiesComboBox, BorderLayout.CENTER);
		galaxiesPanel.add(galaxiesRightPanel, BorderLayout.EAST);
		contentListerPanel.add(galaxiesPanel);
		contentListerPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

		solarSystemsComboBox = new JComboBox<>(solarSystems);
		solarSystemsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		solarSystemsComboBox.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof SolarSystem) {
					setText(((SolarSystem) value).getName());
				}
				return component;
			}
		});
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

		addSolarSystemButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField solarSystemName = new JTextField();
				JTextField starName = new JTextField();
				JTextField starType = new JTextField();
				final JComponent[] inputs = new JComponent[] { new JLabel("Name of the solar system:"), solarSystemName,
						new JLabel("Name of the star:"), starName, new JLabel("Type of the star:"), starType };
				int result = JOptionPane.showConfirmDialog(null, inputs, "Add a solar system", JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					try {
						us.addSolarSystemToGalaxy(((Galaxy) galaxiesComboBox.getSelectedItem()).getName(), solarSystemName.getText(),
								starName.getText(), starType.getText());
						updateSolarSystemsComboBox(us);

					} catch (UniverseException e1) {
						JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		deleteSolarSystemButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					us.deleteSolarSystem(((SolarSystem) solarSystemsComboBox.getSelectedItem()).getName());
					updateSolarSystemsComboBox(us);
					updateStarComboBox(us);
				} catch (UniverseException e1) {
					JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
				}

			}
		});

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
		solarSystemsPanel.add(new JLabel("Solar system: "), BorderLayout.NORTH);
		solarSystemsPanel.add(solarSystemsComboBox, BorderLayout.CENTER);
		solarSystemsPanel.add(solarSystemsRightPanel, BorderLayout.EAST);
		contentListerPanel.add(solarSystemsPanel);
		contentListerPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

		starComboBox = new JComboBox<>(new Star[] { star });
		starComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		starComboBox.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Star) {
					Star star = (Star) value;
					setText(star.getName() + " (" + star.getType() + ")");
				}
				return component;
			}
		});
		contentListerPanel.add(starComboBox);
		contentListerPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

		planetsComboBox = new JComboBox<>(planets);
		planetsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		planetsComboBox.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Planet) {
					Planet planet = (Planet) value;

					StringBuilder sb = new StringBuilder();

					sb.append(planet.getName());
					sb.append(" (radius: ");
					sb.append(planet.getRadius().getValue());
					sb.append(" ");
					sb.append(planet.getRadius().getUnit());
					sb.append(", orbitalPeriod: ");
					sb.append(planet.getOrbitalPeriod().getValue());
					sb.append(" ");
					sb.append(planet.getOrbitalPeriod().getUnit());
					sb.append(", orbitalSpeed: ");
					sb.append(planet.getOrbitalSpeed().getValue());
					sb.append(" ");
					sb.append(planet.getOrbitalSpeed().getValue());
					sb.append(", eccentricity: ");
					sb.append(planet.getEccentricity().getValue());
					sb.append(" ");
					sb.append(planet.getEccentricity().getUnit());
					sb.append(", semiMajorAxis: ");
					sb.append(planet.getSemiMajorAxis().getValue());
					sb.append(" ");
					sb.append(planet.getSemiMajorAxis().getUnit());
					sb.append(", mass: ");
					sb.append(planet.getMass().getValue());
					sb.append(" ");
					sb.append(planet.getMass().getUnit());
					sb.append(")");

					setText(sb.toString());
				}
				return component;
			}
		});
		planetsComboBox.addActionListener(e -> {
			updateMoonsComboBox(us);
		});

		JButton addPlanetButton = new JButton("Add planet");
		addPlanetButton.setPreferredSize(buttonPreferredSize);
		JButton updatePlanetButton = new JButton("Update planet");
		updatePlanetButton.setPreferredSize(buttonPreferredSize);
		JButton deletePlanetButton = new JButton("Delete planet");
		deletePlanetButton.setPreferredSize(buttonPreferredSize);

		addPlanetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField planetName = new JTextField();
				JPanel radiusPanel = new JPanel();
				radiusPanel.setLayout(new GridLayout(1, 2));
				JTextField radius = new JTextField();
				JTextField radiusUnit = new JTextField("km");
				radiusPanel.add(radius);
				radiusPanel.add(radiusUnit);
				JPanel orbitalPeriodPanel = new JPanel();
				orbitalPeriodPanel.setLayout(new GridLayout(1, 2));
				JTextField orbitalPeriod = new JTextField();
				JTextField orbitalPeriodUnit = new JTextField("day");
				orbitalPeriodPanel.setLayout(new GridLayout(1, 2));
				orbitalPeriodPanel.add(orbitalPeriod);
				orbitalPeriodPanel.add(orbitalPeriodUnit);
				JPanel orbitalSpeedPanel = new JPanel();
				orbitalSpeedPanel.setLayout(new GridLayout(1, 2));
				JTextField orbitalSpeed = new JTextField();
				JTextField orbitalSpeedUnit = new JTextField("kps");
				orbitalSpeedPanel.add(orbitalSpeed);
				orbitalSpeedPanel.add(orbitalSpeedUnit);
				JPanel eccentricityPanel = new JPanel();
				eccentricityPanel.setLayout(new GridLayout(1, 2));
				JTextField eccentricity = new JTextField();
				eccentricityPanel.add(eccentricity);
				JPanel semiMajorAxisPanel = new JPanel();
				semiMajorAxisPanel.setLayout(new GridLayout(1, 2));
				JTextField semiMajorAxis = new JTextField();
				JTextField semiMajorAxisUnit = new JTextField("AU");
				semiMajorAxisPanel.add(semiMajorAxis);
				semiMajorAxisPanel.add(semiMajorAxisUnit);
				JPanel massPanel = new JPanel();
				massPanel.setLayout(new GridLayout(1, 2));
				JTextField mass = new JTextField();
				JTextField massUnit = new JTextField("kg");
				massPanel.add(mass);
				massPanel.add(massUnit);
				final JComponent[] inputs = new JComponent[] { new JLabel("Name of the Planet:"), planetName, new JLabel("Radius:"),
						radiusPanel, new JLabel("Orbital period:"), orbitalPeriodPanel, new JLabel("Orbital speed:"), orbitalSpeedPanel,
						new JLabel("Eccentricity:"), eccentricityPanel, new JLabel("Semi-major axis:"), semiMajorAxisPanel,
						new JLabel("Mass:"), massPanel

				};
				int result = JOptionPane.showConfirmDialog(null, inputs, "Add a planet", JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					try {
						us.addPlanetToSolarSystem(((SolarSystem) solarSystemsComboBox.getSelectedItem()).getName(), planetName.getText(),
								new Property(radiusUnit.getText(), Double.parseDouble(radius.getText())),
								new Property(orbitalPeriodUnit.getText(), Double.parseDouble(orbitalPeriod.getText())),
								new Property(orbitalSpeedUnit.getText(), Double.parseDouble(orbitalSpeed.getText())),
								new Property(null, Double.parseDouble(eccentricity.getText())),
								new Property(semiMajorAxisUnit.getText(), Double.parseDouble(semiMajorAxis.getText())),
								new Property(massUnit.getText(), Double.parseDouble(mass.getText())));
						updatePlanetsComboBox(us);

					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		deletePlanetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					us.deletePlanet(((Planet) planetsComboBox.getSelectedItem()).getName());
					updatePlanetsComboBox(us);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
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
		contentListerPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

		moonsComboBox = new JComboBox<>(moons);
		moonsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		moonsComboBox.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Moon) {
					Moon moon = (Moon) value;

					StringBuilder sb = new StringBuilder();
					sb.append(moon.getName());
					sb.append(" (");
					sb.append(moon.getRadius().getValue());
					sb.append(" ");
					sb.append(moon.getRadius().getUnit());
					sb.append(")");

					setText(sb.toString());
				}
				return component;
			}
		});

		JButton addMoonButton = new JButton("Add moon");
		addMoonButton.setPreferredSize(buttonPreferredSize);
		JButton updateMoonButton = new JButton("Update moon");
		updateMoonButton.setPreferredSize(buttonPreferredSize);
		JButton deleteMoonButton = new JButton("Delete moon");
		deleteMoonButton.setPreferredSize(buttonPreferredSize);

		addMoonButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField moonName = new JTextField();
				JPanel radiusPanel = new JPanel();
				radiusPanel.setLayout(new GridLayout(1, 2));
				JTextField radius = new JTextField();
				JTextField radiusUnit = new JTextField("km");
				radiusPanel.add(radius);
				radiusPanel.add(radiusUnit);
				final JComponent[] inputs = new JComponent[] { new JLabel("Name of moon:"), moonName, new JLabel("Radius:"), radiusPanel };
				int result = JOptionPane.showConfirmDialog(null, inputs, "Add a moon", JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					try {
						us.addMoonToPlanet(((Planet) planetsComboBox.getSelectedItem()).getName(), moonName.getText(),
								new Property(radiusUnit.getText(), Double.parseDouble(radius.getText())));
						updateMoonsComboBox(us);

					} catch (UniverseException e1) {
						JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		deleteMoonButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					us.deleteMoon(((Moon) moonsComboBox.getSelectedItem()).getName());
					updateMoonsComboBox(us);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
				}

			}
		});
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
		contentListerPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

		cometsComboBox = new JComboBox<>(comets);
		cometsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		cometsComboBox.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Comet) {
					Comet comet = (Comet) value;

					StringBuilder sb = new StringBuilder();

					sb.append(comet.getName());
					sb.append(" (");
					sb.append(comet.getOrbitalPeriod().getValue());
					sb.append(" ");
					sb.append(comet.getOrbitalPeriod().getUnit());
					sb.append(")");

					setText(sb.toString());
				}
				return component;
			}
		});
		cometsComboBox.addActionListener(e -> {
			updateMineralsComboBox(us);
		});

		JButton addCometButton = new JButton("Add comet");
		addCometButton.setPreferredSize(buttonPreferredSize);
		JButton updateCometButton = new JButton("Update comet");
		updateCometButton.setPreferredSize(buttonPreferredSize);
		JButton deleteCometButton = new JButton("Delete comet");
		deleteCometButton.setPreferredSize(buttonPreferredSize);

		addCometButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField cometName = new JTextField();
				JPanel radiusPanel = new JPanel();
				radiusPanel.setLayout(new GridLayout(1, 2));
				JTextField radius = new JTextField();
				JTextField radiusUnit = new JTextField("km");
				radiusPanel.add(radius);
				radiusPanel.add(radiusUnit);
				final JComponent[] inputs = new JComponent[] { new JLabel("Name of comet:"), cometName, new JLabel("Radius:"),
						radiusPanel };
				int result = JOptionPane.showConfirmDialog(null, inputs, "Add a comet", JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					try {
						us.addCometToSolarSystem(((SolarSystem) solarSystemsComboBox.getSelectedItem()).getName(), cometName.getText(),
								new Property(radiusUnit.getText(), Double.parseDouble(radius.getText())));
						updateCometsComboBox(us);

					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		deleteCometButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					us.deleteComet(((Comet) cometsComboBox.getSelectedItem()).getName());
					updateCometsComboBox(us);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
				}
			}
		});

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
		contentListerPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

		mineralsComboBox = new JComboBox<>(minerals);
		mineralsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		mineralsComboBox.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Mineral) {
					Mineral mineral = (Mineral) value;

					StringBuilder sb = new StringBuilder();

					sb.append(mineral.getElementName());
					sb.append(" (");
					sb.append(mineral.getQuantity().getValue());
					sb.append(" ");
					sb.append(mineral.getQuantity().getUnit());
					sb.append(")");

					setText(sb.toString());
				}
				return component;
			}
		});

		JButton addMineralButton = new JButton("Add mineral");
		addMineralButton.setPreferredSize(buttonPreferredSize);
		JButton updateMineralButton = new JButton("Update mineral");
		updateMineralButton.setPreferredSize(buttonPreferredSize);
		JButton deleteMineralButton = new JButton("Delete mineral");
		deleteMineralButton.setPreferredSize(buttonPreferredSize);

		addMineralButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField mineralName = new JTextField();
				JPanel quantityPanel = new JPanel();
				quantityPanel.setLayout(new GridLayout(1, 2));
				JTextField quantity = new JTextField();
				JTextField quantityUnit = new JTextField("g");
				quantityPanel.add(quantity);
				quantityPanel.add(quantityUnit);
				final JComponent[] inputs = new JComponent[] { new JLabel("Name of mineral:"), mineralName, new JLabel("Quantity:"),
						quantityPanel };
				int result = JOptionPane.showConfirmDialog(null, inputs, "Add a mineral", JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					try {
						us.addMineralToComet(((Comet) cometsComboBox.getSelectedItem()).getName(), mineralName.getText(),
								new Property(quantityUnit.getText(), Double.parseDouble(quantity.getText())));
						updateMineralsComboBox(us);

					} catch (Exception e1) {
						JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		deleteMineralButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					us.deleteMineralOnComet(((Comet)cometsComboBox.getSelectedItem()).getName(), ((Mineral)mineralsComboBox.getSelectedItem()).getElementName());
					updateMineralsComboBox(us);
				} catch (UniverseException e1) {
					JOptionPane.showMessageDialog(frame, e1.getClass().getName(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
					
				}
			}
		});
		
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
