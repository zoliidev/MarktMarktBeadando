package hu.marktmarkt.beadando.Collection;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileManager {
    public void FileKi(String data, Context context, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Write hiba!", "Fájl kiírás közben hiba történt: " + e);
        }
    }
    public String fileOlvas(Context context, String fileName) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "A fájl nem található: " + e.getMessage());
        } catch (IOException e) {
            Log.e("login activity", "A fájl nem olvasható: " + e.getMessage());
        }

        return ret;
    }
}
