import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Collections;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    public static Image slikaRampa = new Image("rampa.png");
    public static GridPane gridPane1 = new GridPane();
    public static ImageView carinski1Slika = new ImageView(slikaRampa);
    public static ImageView carinski2Slika = new ImageView(slikaRampa);
    public static ImageView policijski1Slika = new ImageView(slikaRampa);
    public static ImageView policijski2Slika = new ImageView(slikaRampa);
    public static ImageView policijskiZaKamioneSlika = new ImageView(slikaRampa);
    public static StackPane carinski1 = new StackPane();
    public static StackPane carinski2 = new StackPane();
    public static StackPane policijski1 = new StackPane();
    public static StackPane policijski2 = new StackPane();
    public static StackPane policijskiZaKamione = new StackPane();
    public static VBox red = new VBox();
    public static TilePane tilePaneDrugeScene = new TilePane();
    public static TilePane tilePaneTreceScene = new TilePane();
    public static int vrijemeUSekundama = 0;
    public static String vrijemeTrajanja = "";

    @Override
    public void start(Stage stage) {
        try {
            Image ikonica = new Image("ikonica1.png");
            stage.getIcons().add(ikonica);

            //PRVA SCENA//
            StackPane root = new StackPane();
            Image pozadina1 = new Image("pozadina1.jpeg");
            BackgroundImage backgroundImage1 = new BackgroundImage(pozadina1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            Background background1 = new Background(backgroundImage1);

            Scene prvaScena = new Scene(root, 500, 650);
            root.setBackground(background1);

            //DODAVANJE GRID PANEA NA PRVU SCENU
            for (int i = 0; i < 4; i++) {

                RowConstraints row = new RowConstraints(100);
                gridPane1.getRowConstraints().add(row);

                ColumnConstraints col = new ColumnConstraints(100);
                gridPane1.getColumnConstraints().add(col);
            }
            RowConstraints lastRowConstraints = new RowConstraints(300); // Širi poslednji red (duplo širi)
            gridPane1.getRowConstraints().add(lastRowConstraints);

            Button dugmeZaDruguScenu = new Button("Druga scena");
            GridPane.setRowIndex(dugmeZaDruguScenu, 0);
            GridPane.setColumnIndex(dugmeZaDruguScenu, 4);
            gridPane1.getChildren().add(dugmeZaDruguScenu);

            //DODAVANJE TERMINALA CARINSKIH
            carinski1Slika.setFitWidth(90);
            carinski1Slika.setFitHeight(100);
            carinski1.getChildren().add(carinski1Slika);
            GridPane.setRowIndex(carinski1, 1);
            GridPane.setColumnIndex(carinski1, 1);
            gridPane1.getChildren().add(carinski1);

            carinski2Slika.setFitWidth(90);
            carinski2Slika.setFitHeight(100);
            carinski2.getChildren().add(carinski2Slika);
            GridPane.setRowIndex(carinski2, 1);
            GridPane.setColumnIndex(carinski2, 3);
            gridPane1.getChildren().add(carinski2);

            //DODAVANJE POLICIJSKIH TERMINALA
            policijski1Slika.setFitWidth(90);
            policijski1Slika.setFitHeight(100);
            policijski1.getChildren().add(policijski1Slika);
            GridPane.setRowIndex(policijski1, 2);
            GridPane.setColumnIndex(policijski1, 0);
            gridPane1.getChildren().add(policijski1);

            policijski2Slika.setFitWidth(90);
            policijski2Slika.setFitHeight(100);
            policijski2.getChildren().add(policijski2Slika);
            GridPane.setRowIndex(policijski2, 2);
            GridPane.setColumnIndex(policijski2, 2);
            gridPane1.getChildren().add(policijski2);

            policijskiZaKamioneSlika.setFitWidth(90);
            policijskiZaKamioneSlika.setFitHeight(100);
            policijskiZaKamione.getChildren().add(policijskiZaKamioneSlika);
            GridPane.setRowIndex(policijskiZaKamione, 2);
            GridPane.setColumnIndex(policijskiZaKamione, 4);
            gridPane1.getChildren().add(policijskiZaKamione);

            //DODAVANJE GRANICNOG REDA
            GridPane.setRowIndex(red, 4);
            GridPane.setColumnIndex(red, 2);
            gridPane1.getChildren().add(red);

            Button dugmeZaPauzu = new Button("Start");
            GridPane.setRowIndex(dugmeZaPauzu, 0);
            GridPane.setColumnIndex(dugmeZaPauzu, 0);
            gridPane1.getChildren().add(dugmeZaPauzu);

            //VRIJEME TRAJANJA SIMULACIJE
            Label vrijemeTrajanjaSimulacije = new Label();
            vrijemeTrajanjaSimulacije.setStyle("-fx-background-color: white;");
            vrijemeTrajanjaSimulacije.setPrefWidth(60);
            vrijemeTrajanjaSimulacije.setPrefHeight(10);
            vrijemeTrajanjaSimulacije.setAlignment(Pos.CENTER);

            Timer timer = new Timer(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!Simulacija.pauza) {
                        vrijemeUSekundama++;
                        int minute = vrijemeUSekundama / 60;
                        int sekunde = vrijemeUSekundama % 60;
                        vrijemeTrajanja = String.format("%02d:%02d", minute, sekunde);
                        Platform.runLater(() -> {
                            vrijemeTrajanjaSimulacije.setText(vrijemeTrajanja);
                        });
                    }
                }
            };
            timer.scheduleAtFixedRate(timerTask, 1000, 1000);

            GridPane.setRowIndex(vrijemeTrajanjaSimulacije, 4);
            GridPane.setColumnIndex(vrijemeTrajanjaSimulacije, 4);
            gridPane1.getChildren().add(vrijemeTrajanjaSimulacije);

            dugmeZaPauzu.setOnAction(e -> {
                Simulacija.pauza = !Simulacija.pauza;
                if (!Simulacija.pauza) {
                    dugmeZaPauzu.setText("Pauza");
                    synchronized (Simulacija.lock) {
                        Simulacija.lock.notifyAll();
                    }
                } else {
                    dugmeZaPauzu.setText("Start");
                }
            });

            root.getChildren().add(gridPane1);
            stage.setScene(prvaScena);

            //DRUGA SCENA//

            StackPane root2 = new StackPane();
            Scene drugaScena = new Scene(root2, 700, 500);

            Image parking = new Image("parking.png");
            BackgroundImage backgroundImage2 = new BackgroundImage(parking, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            Background background2 = new Background(backgroundImage2);
            root2.setBackground(background2);

            dugmeZaDruguScenu.setOnAction(e -> {
                stage.setScene(drugaScena);
            });

            Button dugmeZaPrvuScenu = new Button("Prva scena");
            GridPane.setRowIndex(dugmeZaPrvuScenu, 0);
            GridPane.setColumnIndex(dugmeZaPrvuScenu, 7);

            dugmeZaPrvuScenu.setOnAction(e -> {
                stage.setScene(prvaScena);
            });

            Button dugmeZaTrecuScenu = new Button("Treća scena");
            GridPane.setRowIndex(dugmeZaTrecuScenu, 0);
            GridPane.setColumnIndex(dugmeZaTrecuScenu, 8);

            //POSTAVLJANJE BUTTONA NA DRUGOJ SCENI
            root2.setAlignment(dugmeZaTrecuScenu, Pos.TOP_RIGHT);
            root2.setMargin(dugmeZaTrecuScenu, new Insets(10.0, 10.0, 0.0, 10.0));


            root2.setAlignment(dugmeZaPrvuScenu, Pos.TOP_LEFT);
            root2.setMargin(dugmeZaPrvuScenu, new Insets(10.0, 10.0, 0.0, 10.0));

            root2.setAlignment(tilePaneDrugeScene, Pos.BOTTOM_CENTER);
            root2.setMargin(tilePaneDrugeScene, new Insets(100.0, 10.0, 0.0, 10.0));

            root2.getChildren().addAll(dugmeZaPrvuScenu, dugmeZaTrecuScenu, tilePaneDrugeScene);

            //TRECA SCENA//
            //SLUZI ZA PRIKAZ VOZILA KOJA SU IMALA INCIDENT

            StackPane root3 = new StackPane();
            Scene trecaScena = new Scene(root3, 600, 600);

            Image parking1 = new Image("trecaScena.png");
            BackgroundImage backgroundImage3 = new BackgroundImage(parking1, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            Background background3 = new Background(backgroundImage3);
            root3.setBackground(background3);

            dugmeZaTrecuScenu.setOnAction(e -> {
                stage.setScene(trecaScena);
            });

            //DODAVANJE BUTTONA NA TRECU SCENU
            Button dugmeZaDruguScenuIzTrece = new Button("Druga scena");
            root3.setAlignment(dugmeZaDruguScenuIzTrece, Pos.TOP_LEFT);
            root3.setMargin(dugmeZaDruguScenuIzTrece, new Insets(10.0, 10.0, 0.0, 10.0));

            root3.setAlignment(tilePaneTreceScene, Pos.BOTTOM_CENTER);
            root3.setMargin(tilePaneTreceScene, new Insets(100.0, 10.0, 0.0, 10.0));

            dugmeZaDruguScenuIzTrece.setOnAction(e -> {
                stage.setScene(drugaScena);
            });

            root3.getChildren().addAll(dugmeZaDruguScenuIzTrece, tilePaneTreceScene);

            stage.setOnCloseRequest(event-> {
                System.exit(0);
            });

            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
        }
    }
    public static String ispisiInfoOVozilu(Vozilo vozilo) {
        String ispis = "";
        if (vozilo instanceof AutobusInterface) {
            ispis = "Autobus" + vozilo.toString() + "\n" + "Broj putnika: " + vozilo.getBrojPutnika() + "\n"
                    + "Vozač: " + vozilo.getVozac().getIdentifikacioniDokument() + "\n";
            if (vozilo.getBrojPutnika() != 0) {
                ispis+="Putnici: ";
                for (int i = 0; i < vozilo.getBrojPutnika(); i++) {
                    ispis += vozilo.getPutnici().get(i) + " ima kofer: " + vozilo.getPutnici().get(i).ImaLiKofer() + " ";
                    if (vozilo.getPutnici().get(i).ImaLiKofer()) {
                        ispis += "ima nedozvoljene stvari u koferu: " + vozilo.getPutnici().get(i).getKofer().ImaLiNedozvoljeneStvari() + "\n";
                    }
                }
            }
        } else if (vozilo instanceof AutomobilInterface) {
            ispis = "Auto" + vozilo.toString() + "\nBroj putnika: " + vozilo.getBrojPutnika() + "\n"
                    + "Vozač: " + vozilo.getVozac().getIdentifikacioniDokument() + "\n";
            if (vozilo.getBrojPutnika() != 0) {
                ispis+="Putnici: ";
                for (int i = 0; i < vozilo.getBrojPutnika(); i++) {
                    ispis += vozilo.getPutnici().get(i);
                }
            }
        } else if (vozilo instanceof KamionInterface) {
            Kamion kamion = (Kamion) vozilo;
            ispis = "Kamion" + kamion.toString() + "\nBroj putnika: " + vozilo.getBrojPutnika() + "\n"
                    + "Vozač: " + vozilo.getVozac().getIdentifikacioniDokument() + "\n"
                    + "ima nedozvoljeni teret: " + kamion.daLiKamionImaVeciTeret() + "\n"
                    + "potrebna carinska dokumentacija: " + kamion.daLiJePotrebnaCarinskaDokumentacija();
        }
        return ispis;
    }

    //DODAVANJE U GRANICNI RED
    public static void dodajURedNaPrvuScenu(Vozilo vozilo) {
        Platform.runLater(() -> {
            if (vozilo instanceof Automobil) {

                Image auto = new Image("auto.png");
                ImageView autoImage = new ImageView(auto);
                autoImage.setFitWidth(50);
                autoImage.setFitHeight(50);

                Tooltip tooltip = new Tooltip(ispisiInfoOVozilu(vozilo));
                Tooltip.install(autoImage, tooltip);
                autoImage.setOnMouseEntered(event -> tooltip.show(autoImage, event.getScreenX(), event.getScreenY() + 15));
                autoImage.setOnMouseExited(event -> tooltip.hide());

                red.getChildren().add(autoImage);

            } else if (vozilo instanceof Autobus) {

                Image autobus = new Image("autobus.png");
                ImageView autobusImage = new ImageView(autobus);
                autobusImage.setFitWidth(50);
                autobusImage.setFitHeight(50);

                Tooltip tooltip = new Tooltip(ispisiInfoOVozilu(vozilo));
                Tooltip.install(autobusImage, tooltip);
                autobusImage.setOnMouseEntered(event -> tooltip.show(autobusImage, event.getScreenX(), event.getScreenY() + 15));
                autobusImage.setOnMouseExited(event -> tooltip.hide());

                red.getChildren().add(autobusImage);

            } else if (vozilo instanceof Kamion) {

                Image kamion = new Image("kamion.png");
                ImageView kamionImage = new ImageView(kamion);
                kamionImage.setFitHeight(50);
                kamionImage.setFitWidth(50);

                Tooltip tooltip = new Tooltip(ispisiInfoOVozilu(vozilo));
                Tooltip.install(kamionImage, tooltip);
                kamionImage.setOnMouseEntered(event -> tooltip.show(kamionImage, event.getScreenX(), event.getScreenY() + 15));
                kamionImage.setOnMouseExited(event -> tooltip.hide());

                red.getChildren().add(kamionImage);
            }
        });
    }

    //DODAVANJE OSTATKA REDA NA DRUGU SCENU
    public static void dodajNaDruguScenu(Vozilo vozilo) {
        Platform.runLater(() -> {
            if (vozilo instanceof AutomobilInterface) {

                Image auto = new Image("auto.png");
                ImageView autoImage = new ImageView(auto);

                Tooltip tooltip = new Tooltip(ispisiInfoOVozilu(vozilo));
                Tooltip.install(autoImage, tooltip);
                autoImage.setOnMouseEntered(event -> tooltip.show(autoImage, event.getScreenX(), event.getScreenY() + 15));
                autoImage.setOnMouseExited(event -> tooltip.hide());

                autoImage.setFitWidth(50);
                autoImage.setFitHeight(50);

                tilePaneDrugeScene.getChildren().add(autoImage);

            } else if (vozilo instanceof AutobusInterface) {

                Image autobus = new Image("autobus.png");
                ImageView autobusImage = new ImageView(autobus);

                autobusImage.setFitWidth(50);
                autobusImage.setFitHeight(50);

                Tooltip tooltip = new Tooltip(ispisiInfoOVozilu(vozilo));
                Tooltip.install(autobusImage, tooltip);
                autobusImage.setOnMouseEntered(event -> tooltip.show(autobusImage, event.getScreenX(), event.getScreenY() + 15));
                autobusImage.setOnMouseExited(event -> tooltip.hide());

                tilePaneDrugeScene.getChildren().add(autobusImage);
            } else if (vozilo instanceof KamionInterface) {

                Image kamion = new Image("kamion.png");
                ImageView kamionImage = new ImageView(kamion);

                kamionImage.setFitHeight(50);
                kamionImage.setFitWidth(50);

                Tooltip tooltip = new Tooltip(ispisiInfoOVozilu(vozilo));
                Tooltip.install(kamionImage, tooltip);
                kamionImage.setOnMouseEntered(event -> tooltip.show(kamionImage, event.getScreenX(), event.getScreenY() + 15));
                kamionImage.setOnMouseExited(event -> tooltip.hide());

                tilePaneDrugeScene.getChildren().add(kamionImage);
            }
        });
    }
    public static void prebaciIzRedaNaPolicijski(int brojTerminala) {
        Platform.runLater(() -> {
            Node vozilo = red.getChildren().get(0);
            if (brojTerminala == 1) {

                StackPane p1 = new StackPane();
                p1.getChildren().add(vozilo);
                policijski1.getChildren().add(p1);

            } else if (brojTerminala == 2) {

                StackPane p2 = new StackPane();
                p2.getChildren().add(vozilo);
                policijski2.getChildren().add(p2);

            } else if (brojTerminala == 3) {

                StackPane p3 = new StackPane();
                p3.getChildren().add(vozilo);
                policijskiZaKamione.getChildren().add(p3);

            }
            if (!tilePaneDrugeScene.getChildren().isEmpty()) {

                vozilo = tilePaneDrugeScene.getChildren().get(0);
                red.getChildren().add(vozilo);
            }
        });
    }

    public static void prebaciSaPolicijskihNaCarinski(int brojTerminalaSaKogDolazi) {
        Platform.runLater(() -> {
            if (brojTerminalaSaKogDolazi == 1) {
                for (Node vozilo : policijski1.getChildren()) {
                    if (vozilo instanceof StackPane) {

                        carinski1.getChildren().add(vozilo);
                        policijski1.getChildren().remove(vozilo);
                        break;
                    }
                }
            } else if (brojTerminalaSaKogDolazi == 2) {
                for (Node vozilo : policijski2.getChildren()) {
                    if (vozilo instanceof StackPane) {

                        carinski1.getChildren().add(vozilo);
                        policijski2.getChildren().remove(vozilo);
                        break;
                    }
                }
            } else if (brojTerminalaSaKogDolazi == 3) {
                for (Node vozilo : policijskiZaKamione.getChildren()) {
                    if (vozilo instanceof StackPane) {

                        carinski2.getChildren().add(vozilo);
                        policijskiZaKamione.getChildren().remove(vozilo);
                        break;
                    }
                }
            }
        });
    }

    //SLUCAJ KADA VOZILO PADA NA POLICIJSKOM TERMINALU
    public static void izbrisiSaPolicijskog(int brojPolicijskog) {
        Platform.runLater(() -> {
            if (brojPolicijskog == 1) {
                policijski1.getChildren().remove(policijski1.getChildren().size() - 1);
            } else if (brojPolicijskog == 2) {
                policijski2.getChildren().remove(policijski2.getChildren().size() - 1);
            } else if (brojPolicijskog == 3) {
                policijskiZaKamione.getChildren().remove(policijskiZaKamione.getChildren().size() - 1);
            }
        });
    }

    public static void izbrisiSaCarinskog(int brojCarinskog) {
        Platform.runLater(() -> {
            if (brojCarinskog == 1) {
                carinski1.getChildren().remove(carinski1.getChildren().size() - 1);
            } else if (brojCarinskog == 2) {
                carinski2.getChildren().remove(carinski2.getChildren().size() - 1);
            }
        });
    }

    public static void dodajNaTrecuScenu(Vozilo vozilo, String opisProblema) {
        Platform.runLater(() -> {
            if (vozilo instanceof AutomobilInterface) {

                Image auto = new Image("auto.png");
                ImageView autoImage = new ImageView(auto);

                Tooltip tooltip = new Tooltip(opisProblema);
                Tooltip.install(autoImage, tooltip);
                autoImage.setOnMouseEntered(event -> tooltip.show(autoImage, event.getScreenX(), event.getScreenY() + 15));
                autoImage.setOnMouseExited(event -> tooltip.hide());

                autoImage.setFitWidth(80);
                autoImage.setFitHeight(80);

                tilePaneTreceScene.getChildren().add(autoImage);

            } else if (vozilo instanceof AutobusInterface) {

                Image autobus = new Image("autobus.png");
                ImageView autobusImage = new ImageView(autobus);

                autobusImage.setFitWidth(80);
                autobusImage.setFitHeight(80);

                Tooltip tooltip = new Tooltip(opisProblema);
                Tooltip.install(autobusImage, tooltip);
                autobusImage.setOnMouseEntered(event -> tooltip.show(autobusImage, event.getScreenX(), event.getScreenY() + 15));
                autobusImage.setOnMouseExited(event -> tooltip.hide());

                tilePaneTreceScene.getChildren().add(autobusImage);

            } else if (vozilo instanceof KamionInterface) {

                Image kamion = new Image("kamion.png");
                ImageView kamionImage = new ImageView(kamion);

                kamionImage.setFitHeight(80);
                kamionImage.setFitWidth(80);

                Tooltip tooltip = new Tooltip(opisProblema);
                Tooltip.install(kamionImage, tooltip);
                kamionImage.setOnMouseEntered(event -> tooltip.show(kamionImage, event.getScreenX(), event.getScreenY() + 15));
                kamionImage.setOnMouseExited(event -> tooltip.hide());
                tilePaneTreceScene.getChildren().add(kamionImage);
            }
        });
    }

    public static void main(String[] args) {

        WatchTerminalStatus watcher = new WatchTerminalStatus();

        for (int i = 0; i < Simulacija.BROJ_KAMIONA; i++) {
            Simulacija.pomocniRed.add(new Kamion());
        }
        for (int i = 0; i < Simulacija.BROJ_AUTA; i++) {
            Simulacija.pomocniRed.add(new Automobil());
        }
        for (int i = 0; i < Simulacija.BROJ_AUTOBUSA; i++) {
            Simulacija.pomocniRed.add(new Autobus());
        }
        Collections.shuffle(Simulacija.pomocniRed);
        for (int i = 0; i < Simulacija.pomocniRed.size(); i++) {
            Simulacija.granicniRed.add(Simulacija.pomocniRed.get(i));
            if (i < 5) {
                dodajURedNaPrvuScenu(Simulacija.pomocniRed.get(i));
            } else dodajNaDruguScenu(Simulacija.pomocniRed.get(i));
        }

        watcher.start();

        for (Vozilo vozilo : Simulacija.granicniRed) {
            vozilo.start();
        }

        launch(args);
    }
}