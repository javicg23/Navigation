/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.sf.marineapi.nmea.event.AbstractSentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.HDGSentence;
import net.sf.marineapi.nmea.sentence.MDASentence;
import net.sf.marineapi.nmea.sentence.MWVSentence;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.XDRSentence;
import net.sf.marineapi.nmea.util.Measurement;
import net.sf.marineapi.nmea.util.Position;

/**
 *
 * @author jsoler
 */
public class Model {

    //implementa el patron singleton
    // esto asegura que solamente se va a crear una instancia de la clase model
    // y se podra acceder a ella desde cualquier clase del proyecto
    private static Model model;
    //variable para saber el estilo aplicado y poder cambiar la brujula por codigo
    //true -> diurno, false -> nocturno
    private static boolean estilo = true;

    private Model() {
    }

    public static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public static void setEstilo(boolean style) {
        estilo = style;
    }

    public static boolean getEstilo() {
        return estilo;
    }

    //===================================================================
    // CUIDADO, el objeto de la clase SentenceReader se ejecuta en un hilo
    // no se pueden modificar las propiedades de los objetos graficos desde
    // un metodo ejecutado en este hilo
    private SentenceReader reader;

    //True Wind Dir -- direccion del viento respecto al norte
    private final DoubleProperty TWD = new SimpleDoubleProperty();

    public DoubleProperty TWDProperty() {
        return TWD;
    }

    // True Wind Speed -- intensidad de viento
    private final DoubleProperty TWS = new SimpleDoubleProperty();

    public DoubleProperty TWSProperty() {
        return TWS;
    }

    // Temperatura 
    private final DoubleProperty TEMP = new SimpleDoubleProperty();

    public DoubleProperty TEMPProperty() {
        return TEMP;
    }

    //Heading - compas magnetic
    private final DoubleProperty HDG = new SimpleDoubleProperty();

    public DoubleProperty HDGProperty() {
        return HDG;
    }

    // apparent Wind Angle -- angulo aparente del viento
    private final DoubleProperty AWA = new SimpleDoubleProperty();

    public DoubleProperty AWAProperty() {
        return AWA;
    }
    // apparent Wind Speed -- velocidad media del viento
    private final DoubleProperty AWS = new SimpleDoubleProperty();

    public DoubleProperty AWSProperty() {
        return AWS;
    }

    // inclinacion longitudinal de la embarcacion
    private final DoubleProperty PITCH = new SimpleDoubleProperty();

    public DoubleProperty PITCHProperty() {
        return PITCH;
    }

    // inclinacion transversal de la embarcacion
    private final DoubleProperty ROLL = new SimpleDoubleProperty();

    public DoubleProperty ROLLProperty() {
        return ROLL;
    }

    //==================================================================
    // anade todas las propiedades que necesites, en el hilo principal
    // podras anadir listeners sobre estas propiedades que modifquen la interfaz
    // Position -- posicion del GPS
    private final ObjectProperty<Position> GPS = new SimpleObjectProperty();

    public ObjectProperty<Position> GPSProperty() {
        return GPS;
    }

    // COG -- rumbo del GPS
    private final DoubleProperty COG = new SimpleDoubleProperty();

    public DoubleProperty COGProperty() {
        return COG;
    }
    // SOG -- velocidad del GPS
    private final DoubleProperty SOG = new SimpleDoubleProperty();

    public DoubleProperty SOGProperty() {
        return SOG;
    }

    // latitud por GPS
    private final DoubleProperty LAT = new SimpleDoubleProperty();

    public DoubleProperty LATProperty() {
        return LAT;
    }

    // longitud por GPS
    private final DoubleProperty LON = new SimpleDoubleProperty();

    public DoubleProperty LONProperty() {
        return LON;
    }

    //====================================================================
    //anadir tantos sentenceListener como tipos de sentence queremos tratar
    class HDGSentenceListener
            extends AbstractSentenceListener<HDGSentence> {

        @Override
        public void sentenceRead(HDGSentence sentence) {
            // anadimos el codigo necesario para guardar la información de la sentence    
            HDG.set(sentence.getHeading());
        }
    };

    class MDASentenceListener
            extends AbstractSentenceListener<MDASentence> {

        @Override
        public void sentenceRead(MDASentence sentence) {
            // anadimos el codigo necesario para guardar la información de la sentence 
            TWD.set(sentence.getTrueWindDirection());
            TWS.set(sentence.getWindSpeedKnots());
            TEMP.set(sentence.getAirTemperature());

        }
    }

    //========================================================================================
    // anade todas las clases de que extiendan AbstractSentenceListener que necesites
    class RMCSentenceListener
            extends AbstractSentenceListener<RMCSentence> {

        @Override
        public void sentenceRead(RMCSentence sentence) {
            GPS.set(sentence.getPosition());
            COG.set(sentence.getCourse());
            SOG.set(sentence.getSpeed());
        }
    }

    //========================================================================================
    // anade todas las clases de que extiendan AbstractSentenceListener que necesites
    class MWVSentenceListener
            extends AbstractSentenceListener<MWVSentence> {

        @Override
        public void sentenceRead(MWVSentence sentence) {
            AWA.set(sentence.getAngle());
            AWS.set(sentence.getSpeed());
        }
    }

    // anade todas las clases de que extiendan AbstractSentenceListener que necesites
    class XDRSentenceListener
            extends AbstractSentenceListener<XDRSentence> {

        @Override
        public void sentenceRead(XDRSentence sentence) {
            for (Measurement me : sentence.getMeasurements()) {
                if (me.getName().equals("PTCH")) {
                    PITCH.set(me.getValue());
                } else if (me.getName().equals("ROLL")) {
                    ROLL.set(me.getValue());
                }
            }
        }
    }

    // falta por gestiona que solamente hay un senteceReader
    public void addSentenceReader(File file) throws FileNotFoundException {

        InputStream stream = new FileInputStream(file);
        if (reader != null) {  // esto ocurre si ya estamos leyendo un fichero
            reader.stop();
        }
        reader = new SentenceReader(stream);

        //==================================================================
        //============= Registra todos los sentenceListener que necesites
        HDGSentenceListener hdg = new HDGSentenceListener();
        reader.addSentenceListener(hdg);

        MDASentenceListener mda = new MDASentenceListener();
        reader.addSentenceListener(mda);

        RMCSentenceListener rmd = new RMCSentenceListener();
        reader.addSentenceListener(rmd);

        MWVSentenceListener mwv = new MWVSentenceListener();
        reader.addSentenceListener(mwv);

        XDRSentenceListener xdr = new XDRSentenceListener();
        reader.addSentenceListener(xdr);

        //===============================================================
        //===============================================================
        //== Anadimos un exceptionListener para que capture las tramas que 
        // == no tienen parser, ya que no las usamos
        reader.setExceptionListener(e -> {
            System.out.println(e.getMessage());
        });

        //================================================================
        //======== arrancamos el SentenceReader para que empieze a escucha             
        reader.start();
    }
}
