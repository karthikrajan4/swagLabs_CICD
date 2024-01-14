package basePackage.utilities;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import javax.management.AttributeValueExp;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Joiner;

public class ExcelUtil {

	static Sheet excelSheet;
	static Cell cellValue;
	static Row rowValue;
	static Workbook excelBook;
	static List<String> idList = new ArrayList<String>();
	static ArrayList<Object> headerColumn = new ArrayList<Object>();
	static ArrayList<Object> rowData = new ArrayList<Object>();
	static HashMap<String, String> splitContent = new HashMap<String, String>();
	static int scenarioRowNum;
	static int setColumnNum;
	static int numberOfColumns = 0;
	static String sheetName = null;
	static String scenarioId = null;
	static String columnData = null;
	static String attributeValue = null;
	static String excelLocation = System.getProperty("user.dir") + "\\Excel\\Master.xlsx";
	private static final Lock lock = new ReentrantLock();

	// OLD
	public static Workbook openExcel() throws IOException {
		lock.lock();
		try {
			// Read file location
			FileInputStream excelFilePath = new FileInputStream(excelLocation);

			// Create Object for the workbook
			excelBook = new XSSFWorkbook(excelFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return excelBook;
	}

	public static String getRowData(String sheetName, String scenarioId) throws IOException {
		lock.lock();
		try {
			int sheetID = 0;
			ArrayList<Object> sheetNames = new ArrayList<Object>();
			int numberOfRows = 0;
			numberOfColumns = 0;
			scenarioRowNum = 0;

			// Clearing Lists
			idList.clear();
			headerColumn.clear();
			rowData.clear();

			// Go into the Sheet in the workbook
			int sheetCount = excelBook.getNumberOfSheets();
			for (int i = 0; i < sheetCount; i++) {
				sheetNames.add(excelBook.getSheetName(i));
			}
			for (int i = 0; i < sheetCount; i++) {
				if (sheetNames.get(i).toString().equalsIgnoreCase(sheetName)) {
					sheetID = i;
				}
			}

			// OLD
			excelSheet = excelBook.getSheetAt(sheetID);

			// Iterate row and column and get count on the selected sheet
			Iterator<Row> rowIterator = excelSheet.iterator();

			if (rowIterator.hasNext()) {
				Row headerRow = (Row) rowIterator.next();
				// Total Columns
				numberOfColumns = headerRow.getPhysicalNumberOfCells();
			}
			// Total Rows
			numberOfRows = excelSheet.getLastRowNum();

			// Getting Scenario ID Details (First Column)
			while (rowIterator.hasNext()) {
				rowValue = rowIterator.next();
				Iterator<Cell> columnIterator = rowValue.iterator();
				if (columnIterator.hasNext()) {
					idList.add(columnIterator.next().toString().split(Pattern.quote("."))[0]);
				}
			}

			// Getting Header Row Details (First Row - Title)
			for (int i = 0; i < numberOfColumns; i++) {
				headerColumn.add(excelSheet.getRow(0).getCell(i).getStringCellValue());
			}
			// System.out.println("Title data: " + headerColumn);

			// Getting Index Value for the given Scenario ID
			for (int i = 0; i < idList.size(); i++) {
				if (idList.get(i).toString().equalsIgnoreCase(scenarioId)) {
					scenarioRowNum = i + 1;
				}
			}

			// Getting respective row data by giving particular Scenario ID
			int startRow = scenarioRowNum;
			for (int i = 1; i < numberOfColumns; i++) {
				CellType TypeofCell = excelSheet.getRow(startRow).getCell(i).getCellType();

				switch (TypeofCell) {
				// ***Cell Types***
				// STRING
				// Numeric

				case STRING:
					rowData.add(excelSheet.getRow(startRow).getCell(i).getStringCellValue());
					break;

				case NUMERIC:
					rowData.add(excelSheet.getRow(startRow).getCell(i).getNumericCellValue());
					break;
				}
			}
			System.out.println(
					"Out of " + numberOfRows + " rows, " + scenarioId + " is present in row number " + scenarioRowNum);
			// System.out.println("Fetched row Data: " + rowData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return scenarioId;

	}

	public static String getColumnData(String columnHeading) throws IOException {
		lock.lock();
		try {
			int scenarioColumnNum = 0;
			// Getting Index Value for the given Column Title
			for (int i = 0; i < headerColumn.size(); i++) {
				if (headerColumn.get(i).toString().equalsIgnoreCase(columnHeading)) {
					scenarioColumnNum = i - 1;
				}
			}

			// Getting particular Column Data for the Scenario ID
			int fieldDataIndex = scenarioColumnNum;
			columnData = (String) rowData.get(fieldDataIndex);
			int collumnNum = fieldDataIndex + 1;

			System.out.println("Out of " + numberOfColumns + " columns, the title " + columnHeading
					+ " is present in column number " + collumnNum);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return columnData;
	}

	public static String getAttribute(String attrib) {
		lock.lock();
		try {
			String Heading = attrib;
			// split data from the cell
			String str = columnData;
			if (str.contains("\n")) {
				String[] strArray = str.split("\n");
				try {
					for (int i = 0; i < strArray.length; i++) {
						String splitt = strArray[i];
						String key = StringUtils.substringBefore(splitt, ":-");
						String value = StringUtils.substringAfter(splitt, ":-");
						splitContent.put(key, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(
							"The entered attribute is either wrong or not available from the particular cell. Please check again.");
				}
			} else {
				if (str.contains(":-")) {
					try {
						String key = StringUtils.substringBefore(str, ":-");
						String value = StringUtils.substringAfter(str, ":-");
						splitContent.put(key, value);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(
								"The entered attribute is either wrong or not available from the particular cell. Please check again.");
					}
				} else {
					splitContent.put(Heading, str);
				}
			}
			if (splitContent.containsKey(attrib)) {
				System.out.println("The attribure key is: " + attrib);
				attributeValue = splitContent.get(attrib);
			} else {
				System.out.println(
						"Data " + attrib + " is not present in the given attribute. Or, the given attribute is wrong.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return attributeValue;
	}

	public static void writeData(String colTitle, String setValue) throws IOException {
		lock.lock();
		try {
			String writeValue = setValue;
			String writeKey = null;
			String key = null;
			String value = null;
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<>();

			// Getting Index Value for the given Column Title
			for (int i = 0; i < headerColumn.size(); i++) {
				if (headerColumn.get(i).toString().equalsIgnoreCase(colTitle)) {
					setColumnNum = i - 1;
				}
			}
			// Getting current value in the cell
			String currentVal = (String) rowData.get(setColumnNum);
			System.out.println("The current value inside excel is: " + currentVal);

			// Splitting the input if it has ':-'
			System.out.println("Current writeValue is: " + writeValue);
			if (writeValue.contains(":-")) {
				writeKey = StringUtils.substringBefore(writeValue, ":-");
				writeValue = StringUtils.substringAfter(writeValue, ":-");
				System.out.println("updated writeValue is:" + writeValue);

				// split data from the cell - Under Development
				if (currentVal.contains("\n")) {
					String[] strArray = currentVal.split("\n");
					for (int i = 0; i < strArray.length; i++) {
						String splitt = strArray[i];
						key = StringUtils.substringBefore(splitt, ":-");
						value = StringUtils.substringAfter(splitt, ":-");
						orderMap.put(key, value);
						// System.out.println(splitContent.toString());
					}

					if (orderMap.containsKey(writeKey)) {
						orderMap.put(writeKey, writeValue);
					} else {
						System.out.println("That key is not present in the given input.");
					}
					System.out.println("The updated map value is:" + orderMap);

					// Storing back to string from Map
					String map2String = Joiner.on("\n").withKeyValueSeparator(":-").join(orderMap);

					// Updating value in the stored row data - Multiple value
					rowData.set(setColumnNum, map2String);
					System.out.println("The updated value is: " + rowData.get(setColumnNum));

					// updating value in the excel sheet
					Cell cell2Update = excelSheet.getRow(scenarioRowNum).getCell(setColumnNum + 1);
					cell2Update.setCellValue(map2String);
					FileOutputStream fileOut = new FileOutputStream(excelLocation);
					excelBook.write(fileOut);
				} else {
					// Updating value in the stored row data - Single Value
					rowData.set(setColumnNum, writeValue);
					System.out.println("The updated value is: " + rowData.get(setColumnNum));

					// updating value in the excel sheet
					Cell cell2Update = excelSheet.getRow(scenarioRowNum).getCell(setColumnNum + 1);
					cell2Update.setCellValue(writeValue);
					FileOutputStream fileOut = new FileOutputStream(excelLocation);
					excelBook.write(fileOut);
				}
			} else {
				rowData.set(setColumnNum, writeValue);
				System.out.println("The updated value is: " + rowData.get(setColumnNum));

				// updating value in the excel sheet
				Cell cell2Update = excelSheet.getRow(scenarioRowNum).getCell(setColumnNum + 1);
				cell2Update.setCellValue(writeValue);
				FileOutputStream fileOut = new FileOutputStream(excelLocation);
				excelBook.write(fileOut);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public static void closeExcel() throws IOException {
		// Close the excel
		excelBook.close();
	}

}
