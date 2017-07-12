// JavaWrapperSample.java

package samples;
        
import com.oceanoptics.omnidriver.api.wrapper.Wrapper;
import com.oceanoptics.omnidriver.features.boardtemperature.BoardTemperature;
import com.oceanoptics.omnidriver.features.continuousstrobe.ContinuousStrobe;
import com.oceanoptics.omnidriver.features.ls450functions.LS450_Functions;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * This application demonstrates how to use the Wrapper class to control
 * spectrometers and obtain spectra.
 * <p>This application requires the omniDriver.jar file
 * 
 * <p>This code is provided as-is for illustration only.  Use this code at your own risk.
 * 
 * @author Steve.Shepherd
 */
public class JavaWrapperSample
{
    public final static String APP_TITLE = "Java Wrapper Sample 1.00";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        JavaWrapperSample   myself;
        
        myself = new JavaWrapperSample();
        myself.run();
    }

    private void run()
    {
        int     index;
        int     minimumIntegrationTimeMicroseconds;
        int     numberOfPixels;
        int     numberOfSpectrometersFound;
        int     spectrometerIndex;
        double[] spectrumArray;
        double[] wavelengthArray;
        Wrapper wrapper;
        
        wrapper = new Wrapper();
        numberOfSpectrometersFound = wrapper.openAllSpectrometers();
        if (numberOfSpectrometersFound == -1)
        {
            System.out.println("Exception message: " + wrapper.getLastException());
            System.out.println("Stack trace:\n" + wrapper.getLastExceptionStackTrace());
            return;
        }
        if (numberOfSpectrometersFound == 0)
        {
            System.out.println("No spectrometers were found. Exiting the application.");
            JOptionPane.showMessageDialog(null,"No spectrometers were found. Exiting the application.",APP_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println();
        System.out.println("Number of spectrometers found: " + numberOfSpectrometersFound);
        for (spectrometerIndex=0; spectrometerIndex<numberOfSpectrometersFound; ++spectrometerIndex)
            System.out.println("spectrometer type: " + wrapper.getName(spectrometerIndex) +
                    " s/n: " + wrapper.getSerialNumber(spectrometerIndex) +
                    " number of channels: " + wrapper.getWrapperExtensions().getNumberOfEnabledChannels(spectrometerIndex) +
                    " firmware: " + wrapper.getFirmwareVersion(spectrometerIndex));

        spectrometerIndex = 0; // we will arbitrarily use the first spectrometer we found
        
        // ***** Demonstrate how to use the "board temperature" feature
        BoardTemperature boardTemperature;
        double           temperatureCelsius;
        if (wrapper.isFeatureSupportedBoardTemperature(spectrometerIndex) == true)
        {
            System.out.println("Board temperature feature is supported by this spectrometer.");
            boardTemperature = wrapper.getFeatureControllerBoardTemperature(spectrometerIndex);
            try {
                temperatureCelsius = boardTemperature.getBoardTemperatureCelsius();
                System.out.println("board temperature = " + temperatureCelsius);
            } catch (IOException ioException) {
                System.out.println("The following exception occurred while attempting to obtain the board temperature");
                System.out.println(ioException);
            }
        } else {
            System.out.println("Board temperature feature is not supported by this spectrometer.");
        }
        
        
        // ***** Demonstrate how to use the "Continuous Strobe" feature
        if (wrapper.isFeatureSupportedContinuousStrobe(spectrometerIndex) == true)
        {
            try {
                System.out.println("Continuous-strobe feature is supported by this spectrometer.");
                ContinuousStrobe continuousStrobe;
                continuousStrobe = wrapper.getFeatureControllerContinuousStrobe(spectrometerIndex);
                int delayMicroseconds = 1000; 
                continuousStrobe.setContinuousStrobeDelay(delayMicroseconds);
            } catch (IOException ioException) {
                System.out.println("The following exception occurred while attempting to set the continuous-strobe delay");
                System.out.println(ioException);
            }
        } else {
            System.out.println("Continuous-strobe feature is not supported by this spectrometer.");
        }
        

        // ***** Demonstrate how to use the LS450 feature
        if (wrapper.isFeatureSupportedLS450(spectrometerIndex) == true)
        {
            System.out.println("LS450 feature is supported by this spectrometer.");
            try {
                LS450_Functions ls450Functions;

                ls450Functions = wrapper.getFeatureControllerLS450(spectrometerIndex);
                ls450Functions.setLEDMode(true); // pulsed
            } catch (IOException ioException) {
                System.out.println("The following exception occurred while attempting to set LS450 LED mode");
                System.out.println(ioException);
            }
        } else {
            System.out.println("LS450 feature is not supported by this spectrometer. Is it plugged in?");
        }

        
        // ***** Demonstrate how to set acquisition parameters and acquire a spectrum
        minimumIntegrationTimeMicroseconds = wrapper.getMinimumIntegrationTime(spectrometerIndex);
        wrapper.setIntegrationTime(spectrometerIndex,minimumIntegrationTimeMicroseconds);
        wrapper.setBoxcarWidth(spectrometerIndex,0);
        wrapper.setScansToAverage(spectrometerIndex,1);
        wrapper.setCorrectForElectricalDark(spectrometerIndex,0); // disable electric dark correction
        
        spectrumArray = wrapper.getSpectrum(spectrometerIndex); // HERE IS WHERE THE ACTION IS
        
        wavelengthArray = wrapper.getWavelengths(spectrometerIndex);
        numberOfPixels = spectrumArray.length;
        
        // Display the results
        System.out.println();
        System.out.println("First 100 pixels of the newly acquired spectrum:");
        for (index=0; index<100; ++index)
        {
            System.out.printf("Pixel " + index + " Wavelength:  %1.2f    Value: %f\n", wavelengthArray[index],spectrumArray[index]);
        }
        
        System.out.println();
        System.out.println("Application is exiting normally");
    }
}
