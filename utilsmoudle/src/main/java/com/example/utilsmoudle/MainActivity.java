package com.example.utilsmoudle;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utilsmoudle.TouchUtils.TestCallback;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *  里面加入了Excel中数据的读写
 *  比如把数据生成Excel
 *  从Excel中读取数据等等
 */

public class MainActivity extends AppCompatActivity implements TestCallback.OnFragmentInteractionListener {

    private TextView tv;
    private Button mXlsBtn;

    private Button mXlsRead;


    String[] str = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
    String filename = "textExcel.xls";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        mXlsBtn = (Button) findViewById(R.id.white_xls_btn);
        mXlsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportExcelFile();
            }
        });

        mXlsRead = (Button) findViewById(R.id.read_xls_btn);
        mXlsRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readXlsToProject(MainActivity.this);
            }
        });


        // 步骤1：获取FragmentManager
        FragmentManager fragmentManager = getFragmentManager();

        // 步骤2：获取FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        // 步骤3：创建需要添加的Fragment
        TestCallback testCallback = TestCallback.newInstance("123", "456");
        // 步骤4：动态添加fragment
        // 即将创建的fragment添加到Activity布局文件中定义的占位符中（FrameLayout）
        fragmentTransaction.add(R.id.container_a_c, testCallback);
        fragmentTransaction.commit();

    }

    /**
     * 从excel中读取数据的方法
     *
     * @param context
     */
    public void readXlsToProject(Context context) {

        try {

            File file = new File(Environment.getExternalStorageDirectory() + "/abc.xls");
            FileInputStream ios = new FileInputStream(file);

            Workbook book = new HSSFWorkbook(ios);
            int numberOfSheets = book.getNumberOfSheets();

            Sheet sheet = book.getSheetAt(0);

            String sheetName = sheet.getSheetName();

            /**
             * 获取到excel的行数，全部行数。
             */
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

            int sum = 0;

            for (int i = 0; i < physicalNumberOfRows; i++) {


                /**
                 * 获取某一行一共有多少列，包括空的
                 */
                int columnWidth = sheet.getColumnWidth(i);

                /**
                 * 获取默认行数，不知道什么鬼
                 */
                int defaultColumnWidth = sheet.getDefaultColumnWidth();

                float columnWidthInPixels = sheet.getColumnWidthInPixels(i);

                Row row = sheet.getRow(i);

                short firstCellNum = row.getFirstCellNum();

                int physicalNumberOfCells = row.getPhysicalNumberOfCells();

                short lastCellNum = row.getLastCellNum();


                for (int j = 0; j < physicalNumberOfCells; j++) {


                    Cell cell = row.getCell(j);
                    String s = cell.toString();

                    if (s!=null && s.length()>0){
                        Log.e("123", "sum: " + sum + " i:" + i + " j:" + j + "  stirng:  " + s);
                        sum += 1;
                    }


                }


            }


            Toast.makeText(context, "表的数量：" + numberOfSheets + "   表的名字：" +
                            sheetName + "    physicalNumberOfRows:" + physicalNumberOfRows,
                    Toast.LENGTH_SHORT).show();

            book.close();


        } catch (IOException e) {
            Log.e("123", "出错了");
            e.printStackTrace();
        }


    }

    /**
     * 将数据写入Excel的方法
     */
    public void exportExcelFile() {
        int size = 100;
        Workbook wb = new HSSFWorkbook();
        Sheet sh = wb.createSheet();

        for (int rownum = 0; rownum < size; rownum++) {
            Row row = sh.createRow(rownum);
            for (int cellnum = 0; cellnum < 10; cellnum++) {
                Cell cell = row.createCell(cellnum);
                cell.setCellValue(str[cellnum]);
            }
        }

        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/abc.xls");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            wb.write(fos);
            fos.close();
            Toast.makeText(getApplicationContext(), "导出成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * fragment向activity传递数据，在这个回调里面接收
     *
     * @param uri
     */

    @Override
    public void onFragmentInteraction(String uri) {

        tv.setText(uri);
    }
}
