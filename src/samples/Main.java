 // File:    SpectrometerTest.java
 // @author Ocean Opctics Inc.    JK
 //
 // Date: 04/23/2008
 //
 // Note:
 // This code is provided as-is for illustration only.  Use this code at your own risk.
 // This program written to use the omniDriver files and demonstrate the acquisition 
 // of a spectrum using the wrapper class.
 

package samples;

import com.oceanoptics.omnidriver.api.wrapper.Wrapper;

public class Main {

     public static void main(String[] args) {
       Wrapper wrapper = new Wrapper();             // Create a wrapper object
       int numberOfSpectrometers, numberOfPixels;   // variables for the program
       int integrationTime = 1000;                  // set the integration time
       String serialNumber;                         // variable to get the serial number
       double[] wavelengths, spectralData;          // arrays of doubles to hold the spectral data
                                                    // and wavelengths.
       
       // open the spectrometers and get the serial number of the first one:
       numberOfSpectrometers = wrapper.openAllSpectrometers(); // opens all of the spectrometers and returns the number of spectrometers found
       if (numberOfSpectrometers < 1){              // Check for any spectrometers
           System.out.println("There are no spectrometers attached to the computer");
           return;
       }       
       serialNumber = wrapper.getSerialNumber(0);              // gets the serial number from the first spectrometer
       System.out.println("Serial Number: " + serialNumber);   // prints the serial number to the screen
       
       // get a spectrum from the spectrometers:
       wrapper.setIntegrationTime(0, integrationTime);         // sets the integration time of the first spectrometer
       wavelengths = wrapper.getWavelengths(0);                // gets the wavelengths of the first spectrometer and save them in a double array
       spectralData = wrapper.getSpectrum(0);                  // gets the spectrum from the first spectrometer and saves it to a double array
       numberOfPixels = wrapper.getNumberOfPixels(0);          // gets the number of pixels in the first spectrometer.
       
       // loop for printing the spectral data to the screen:
       for (int i = 0; i < numberOfPixels; i++){       
           System.out.printf("Wavelength: %5.3f,  Intensity: %5.3f %n", wavelengths[i], spectralData[i]);
       }
       
    }

}
