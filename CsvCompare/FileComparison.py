import datetime
import traceback
from os.path import exists
import pandas as pd

# Current date used for output file name
now = datetime.datetime.now()
date = str(now.date())


def csv_comparison(source_file_left=f'source_{date}.csv', target_file_right=f'target_{date}_csv',
                   result_file=f'output{date}.csv'):
    try:
        source = pd.read_csv(source_file_left) # Convert data from the Source CSV to a DataFrame
        target = pd.read_csv(target_file_right) # Convert data from the Target CSV to a DataFrame
        prompt = "Press 'y' or 'n' if you wish to proceed or stop.\n" # Prompt for column check
        left_difference_con_num = len(source.columns)-len(target.columns) # The column number difference between the Source and Target Dataframe
        right_difference_con_num = len(target.columns) - len(source.columns) # The column number difference between the Target and Source Dataframe
        source_difference = set(source.columns.difference(target.columns)) # The column difference between the Source and Target Dataframe
        target_difference = set(target.columns.difference(source.columns)) # The column difference between the Target and Source Dataframe
        if set(source.columns) != set(target.columns) and 0 in {left_difference_con_num, right_difference_con_num}: # Check for a difference in columns when the number is the same for both Source and Target
            print(f"Columns are different between {source_file_left} and {target_file_right}\n"
                f"{source_file_left}: \033[1m{tuple(source.columns)}\033[0m\n{target_file_right}: \033[1m{tuple(target.columns)}\033[0m") # Output explaining the column difference
            function = input(prompt).lower().strip() # input for the prompt to continue or stop that allows spaces or uppercase
            if function == 'n': # check to stop if the user input n or N
                exit()
        elif left_difference_con_num > 0: # Check for the number of columns difference when there is already a column difference between Source and Target
            print(f"{source_file_left} file has these {left_difference_con_num} more column/s {source_difference} than {target_file_right}") # Output explaining the column number difference
            function = input(prompt).lower().strip() # input for the prompt to continue or stop that allows spaces or uppercase
            if function == 'n': # check to stop if the user input n or N
                exit()
        elif right_difference_con_num > 0: # Check for the number of columns difference when there is already a column difference between Target and Source
            print(f"{target_file_right} file has these {right_difference_con_num} more column/s {target_difference} than {source_file_left}") # Output explaining the column number difference
            function = input(prompt).lower().strip() # input for the prompt to continue or stop that allows spaces or uppercase
            if function == 'n': # check to stop if the user input n or N
                exit()
        number = '1' # initialization of a variable used to check for the existence of the output file
        print(f"🕑 Comparing the {source_file_left} and {target_file_right} files") # print to keep the user informed throughout the process
        diff = pd.merge(source, target, how="outer", indicator="Exist") # merge between all the values of the two Dataframes
        """using the Exist indicator to filer only the difference:
        left_only -> the difference between Source and Target with the output being the Source difference
        right_only -> the difference between Target and Source with the output being the Target difference
        !=both -> the differences that are not located in both Dataframes with the output being a mix from Source and Target"""
        diff = diff.loc[diff["Exist"] == "left_only"]
        diff.drop(labels="Exist", axis="columns", inplace=True) # drop of the Exist column after being used to compare
        for i in range(10): # for loop name check for CSV output file
            while exists(result_file): # Check for the existence of the CSV output file
                result_file = 'output' + '_' + date + '_' + number + '.csv' # name of the CSV output file if the while statement is still True
                number = chr(ord(number) + 1) # incrementation for the name check
            else:
                diff.to_csv(path_or_buf=f"{result_file}", encoding='utf-8', index=False, header=True) # result dataframe is made into a CSV file
                break # break out of the for loop
        if diff.empty: # Check if there were any differences
            print("✅ Comparison file generated successfully with NO mismatches") # print if no differences were found between the two files
        elif len(diff.index) == 1: # Check if there is only 1 difference
            print(f"❌ Comparison file generated successfully with {len(diff.index)} mismatch") # print if there is only 1 difference between the two files
        else:
            print(f"❌ Comparison file generated successfully with {len(diff.index)} mismatches") # print if there are many differences between the two files

    except Exception as e: # Exception handling
        tb = traceback.extract_tb((e.__traceback__)) # traceback object to retrieve the line triggering the exception
        first_frame = tb[-1]
        print(f"❌ The following Exception occurred, line {first_frame.lineno} in {first_frame.name}\n{e}") # print of the exception and the line that triggered it

# final call to generate the comparison file
csv_comparison(source_file_left=f"2026-02-18_top_250_imdb_movies.csv", target_file_right=f"2026-02-03_top_250_imdb_movies.csv",
               result_file=f"output_{date}.csv")
