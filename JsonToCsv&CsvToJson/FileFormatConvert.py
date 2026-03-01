import datetime
from os.path import exists
import pandas as pd

# Current date used for output file name
current_date = str(datetime.date.today())


# Function that converts a JSON file to the CSV format
def json_to_csv(json_file, csv_file_path):

    # Convert JSON data to DataFrame
    df = pd.read_json(json_file)

    # Save DataFrame to CSV file
    df.to_csv(csv_file_path, index=False)


# Function that converts a CSV file to the JSON format
def csv_to_json(csv_file, json_file_path):
    # Read and Convert the CSV data to DataFrame
    df = pd.read_csv(csv_file)

    # Save DataFrame to JSON file
    df.to_json(json_file_path, index=False)


# Function that converts a JSON or CSV file to the opposite format based on the user selection
def file_conversion(file):
    number = '1' # initialization of a variable used to check for the existence of the output file
    if file.endswith(".json"): # Check for the CSV input file format
        result = f"output_{current_date}.csv" # Name of the CSV output file
        for i in range(20): # for loop name check for CSV output file
            while exists(result): # Check for the existence of the CSV output file
                result = 'output' + '_' + current_date + '_' + number + '.csv' # name of the CSV output file if the while statement is still True
                number = chr(ord(number)+1) # incrementation for the name check
            else:
                json_to_csv(file, result) # call to the conversion function only if the CSV output file does not exist
                print(f"The {result} file was generated successfully")
                break # break out of the for loop
    elif file.endswith(".csv"): # Check for the input file format
        result = f"output_{current_date}.json" # Name of the JSON output file
        for i in range(10): # for loop name check for JSON output file
            while exists(result): # Check for the existence of the JSON output file
                result = 'output' + '_' + current_date + '_' + number + '.json' # name of the JSON output file if the while statement is still True
                number = chr(ord(number) + 1) # incrementation for the name check
            else:
                csv_to_json(file, result) # call to the conversion function only if the JSON output file does not exist
                print(f"✅ The {result} file was generated successfully")
                break # break out of the for loop
    else:
        print("❌ Input either a JSON or CSV file for format conversion") # print used when no file format or the incorrect file format is used


file_conversion(file=f"2026-02-18_top_250_imdb_movies") # final call to generate the converted file
