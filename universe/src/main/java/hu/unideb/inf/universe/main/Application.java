package hu.unideb.inf.universe.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.xml.xquery.XQConnection;

import hu.unideb.inf.universe.connection.ConnectionUtil;
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

	public static void main(String[] args) throws Exception {
		XQConnection xqc = ConnectionUtil.getConnection("localhost", "1984", "admin", "admin");
		UniverseService us = new UniverseServiceImpl(xqc, "universe");

		JFrame frame = new JFrame("Fejlett XML Technológiák beadandó 2016");
		
		JMenu actionsMenu = new JMenu("Actions");
		actionsMenu.add(new JMenuItem("Add new galaxy"));
		actionsMenu.add(new JMenuItem("Add new solar system"));
		actionsMenu.add(new JMenuItem("Add new planet"));
		actionsMenu.add(new JMenuItem("Add new moon"));
		actionsMenu.add(new JMenuItem("Add new comet"));
		actionsMenu.add(new JMenuItem("Add new mineral"));
		
		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(e -> {
			JDialog dialog = new JDialog(frame, true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			// TODO a fejlesztok neveit itt erdemes lenne felsorolni a korte helyett
			dialog.add(new JButton("korte"));
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
		
		JComboBox<Galaxy> galaxiesComboBox = new JComboBox<>(galaxies);
		galaxiesComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
//		galaxiesComboBox.setRenderer(new ListCellRenderer<Galaxy>() {
//			@Override
//			public Component getListCellRendererComponent(JList<? extends Galaxy> list, Galaxy value, int index,
//					boolean isSelected, boolean cellHasFocus) {
//				// TODO csak a galaxis neve kellene, hogy megjelenjen!
//				return null;
//			}
//		});
		
		JPanel galaxiesButtonPanel = new JPanel(new FlowLayout());
		galaxiesButtonPanel.add(new JButton("Add something"));
		galaxiesButtonPanel.add(new JButton("Update something"));
		galaxiesButtonPanel.add(new JButton("Delete something"));
		
		JPanel galaxiesPanel = new JPanel(new BorderLayout());
		galaxiesPanel.add(galaxiesComboBox, BorderLayout.CENTER);
		galaxiesPanel.add(galaxiesButtonPanel, BorderLayout.EAST);
		contentListerPanel.add(galaxiesPanel);
		
		JComboBox<SolarSystem> solarSystemsComboBox = new JComboBox<>(solarSystems);
		solarSystemsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel solarSystemsButtonPanel = new JPanel(new FlowLayout());
		solarSystemsButtonPanel.add(new JButton("Add something"));
		solarSystemsButtonPanel.add(new JButton("Update something"));
		solarSystemsButtonPanel.add(new JButton("Delete something"));
		
		JPanel solarSystemsPanel = new JPanel(new BorderLayout());
		solarSystemsPanel.add(solarSystemsComboBox, BorderLayout.CENTER);
		solarSystemsPanel.add(solarSystemsButtonPanel, BorderLayout.EAST);
		contentListerPanel.add(solarSystemsPanel);
		
		JComboBox<Star> starComboBox = new JComboBox<>(new Star[] { star } );
		starComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentListerPanel.add(starComboBox);
		
		
		JComboBox<Planet> planetsComboBox = new JComboBox<>(planets);
		planetsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel planetsButtonPanel = new JPanel();
		planetsButtonPanel.add(new JButton("Add something"));
		planetsButtonPanel.add(new JButton("Update something"));
		planetsButtonPanel.add(new JButton("Delete something"));
		
		JPanel planetsPanel = new JPanel(new BorderLayout());
		planetsPanel.add(planetsComboBox, BorderLayout.CENTER);
		planetsPanel.add(planetsButtonPanel, BorderLayout.EAST);
		contentListerPanel.add(planetsPanel);
		
		JComboBox<Moon> moonsComboBox = new JComboBox<>(moons);
		moonsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel moonsButtonPanel = new JPanel();
		moonsButtonPanel.add(new JButton("Add something"));
		moonsButtonPanel.add(new JButton("Update something"));
		moonsButtonPanel.add(new JButton("Delete something"));
		
		JPanel moonsPanel = new JPanel(new BorderLayout());
		moonsPanel.add(moonsComboBox, BorderLayout.CENTER);
		moonsPanel.add(moonsButtonPanel, BorderLayout.EAST);
		contentListerPanel.add(moonsPanel);
		
		JComboBox<Comet> cometsComboBox = new JComboBox<>(comets);
		cometsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel cometsButtonPanel = new JPanel(new FlowLayout());
		cometsButtonPanel.add(new JButton("Add something"));
		cometsButtonPanel.add(new JButton("Update something"));
		cometsButtonPanel.add(new JButton("Delete something"));
		
		JPanel cometsPanel = new JPanel(new BorderLayout());
		cometsPanel.add(cometsComboBox, BorderLayout.CENTER);
		cometsPanel.add(cometsButtonPanel, BorderLayout.EAST);
		contentListerPanel.add(cometsPanel);
		
		JComboBox<Mineral> mineralsComboBox = new JComboBox<>(minerals);
		mineralsComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel mineralsButtonPanel = new JPanel(new FlowLayout());
		mineralsButtonPanel.add(new JButton("Add something"));
		mineralsButtonPanel.add(new JButton("Update something"));
		mineralsButtonPanel.add(new JButton("Delete something"));
		
		JPanel mineralsPanel = new JPanel(new BorderLayout());
		mineralsPanel.add(mineralsComboBox, BorderLayout.CENTER);
		mineralsPanel.add(mineralsButtonPanel, BorderLayout.EAST);
		contentListerPanel.add(mineralsPanel);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(menuBar, BorderLayout.NORTH);
		panel.add(contentListerPanel, BorderLayout.CENTER);
		
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
//		System.out.println("validate: " + us.validate());
//
//		List<Galaxy> galaxies = us.findAllGalaxies();
//		System.out.println("galaxies: " + galaxies);
//
//		List<SolarSystem> solarSystems = us.findAllSolarSystemsInGalaxy(galaxies.get(0));
//		System.out.println("solar system: " + solarSystems);
//
//		Star star = us.findStarInSolarSystem(solarSystems.get(0));
//		System.out.println("star: " + star);
//
//		List<Planet> planets = us.findAllPlanetsInSolarSystem(solarSystems.get(0));
//		System.out.println("planets: " + planets);
//
//		List<Comet> comets = us.findAllCometsInSolarSystem(solarSystems.get(0));
//		System.out.println("comets: " + comets);
//
//		List<Moon> moons = us.findAllMoonsAroundPlanet(planets.get(0));
//		System.out.println("moons: " + moons);
//
//		List<Mineral> minerals = us.findAllMineralsInComet(comets.get(0));
//		System.out.println("minerals: " + minerals);
//
//		Property newQuantity = new Property("kg", 555555.55);
//		us.updateMineralOnComet(comets.get(0).getName(), minerals.get(0).getElementName(), newQuantity);
//		
//		Property quantity = new Property("g", 1024.1024);
//		us.addMineralToComet(comets.get(0).getName(), "Element Zero", quantity);
//
//		minerals = us.findAllMineralsInComet(comets.get(0));
//		System.out.println("minerals: " + minerals);
//
//		Property newOrbitalPeriodProperty = new Property("day", 7.0);
//		us.updateCometOrbitalPeriod(comets.get(0).getName(), newOrbitalPeriodProperty);
//		
//		Property orbitalPeriod = new Property("year", 2520);
//		us.addCometToSolarSystem(solarSystems.get(0).getName(), "Hale–Bopp", orbitalPeriod);
//		
//		Property radius = new Property("km", 2048.1024);
//		us.addMoonToPlanet(planets.get(0).getName(), "Moon-Moon", radius);
//		
//		us.addPlanetToSolarSystem(solarSystems.get(0).getName(), "Mercury", 
//			new Property("km", 2439.7),
//			new Property("day", 87.969),
//			new Property("kps", 47.3621),
//			new Property(null, 0.205630),
//			new Property("AU", 0.387098),
//			new Property("kg", 3.3011 * Math.pow(10, 23)));
//		
//		planets = us.findAllPlanetsInSolarSystem(solarSystems.get(0));
//		System.out.println("planets: " + planets);
//		
//		moons = us.findAllMoonsAroundPlanet(planets.get(0));
//		System.out.println("moons: " + moons);
//		
//		comets = us.findAllCometsInSolarSystem(solarSystems.get(0));
//		System.out.println("comets: " + comets);
//		
//		System.out.println("Deleting mineral: " + minerals.get(0) + " on comet: " + comets.get(0));
//		us.deleteMineralOnComet(comets.get(0).getName(), minerals.get(0).getElementName());
//
//		minerals = us.findAllMineralsInComet(comets.get(0));
//		System.out.println("minerals: " + minerals);
//
//		System.out.println("Deleting comet: " + comets.get(0));
//		us.deleteComet(comets.get(0).getName());
//
//		comets = us.findAllCometsInSolarSystem(solarSystems.get(0));
//		System.out.println("comets: " + comets);
//
//		System.out.println("Deleting moon: " + moons.get(0));
//		us.deleteMoon(moons.get(0).getName());
//
//		moons = us.findAllMoonsAroundPlanet(planets.get(0));
//		System.out.println("moons: " + moons);
//
//		System.out.println("Deleting planet: " + planets.get(0));
//		us.deletePlanet(planets.get(0).getName());
//
//		planets = us.findAllPlanetsInSolarSystem(solarSystems.get(0));
//		System.out.println("planets: " + planets);
//
//		System.out.println("Deleting solar system: " + solarSystems.get(0));
//		us.deleteSolarSystem(solarSystems.get(0).getName());
//
//		solarSystems = us.findAllSolarSystemsInGalaxy(galaxies.get(0));
//		System.out.println("solar system: " + solarSystems);
//
//		System.out.println("Deleting galaxy: " + galaxies.get(0));
//		us.deleteGalaxy(galaxies.get(0).getName());
//
//		galaxies = us.findAllGalaxies();
//		System.out.println("galaxies: " + galaxies);

		xqc.close();
	}

}
