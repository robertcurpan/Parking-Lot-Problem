package structures;

import java.util.ArrayList;

public class FileInputs
{
    private ArrayList<Integer> lotDimensions;
    private ArrayList<String> lotInputs;

    public FileInputs()
    {
        lotDimensions = new ArrayList<Integer>();
        lotInputs = new ArrayList<String>();
    }

    public ArrayList<Integer> getParkingLotDimensions() { return lotDimensions; }
    public ArrayList<String> getParkingLotInputLines() { return lotInputs; }

    public void addLotDimension(Integer dimension) { lotDimensions.add(dimension); }
    public void addLotInput(String inputLine) { lotInputs.add(inputLine); }
}
