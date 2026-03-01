import datetime
import traceback
from os.path import exists
import pandas as pd


now = datetime.datetime.now()
date = str(now.date())


def csv_comparison(source_file_left=f'source_{date}.csv', target_file_right=f'target_{date}_csv',
                   result_file=f'output{date}.csv'):
    try:
        source = pd.read_csv(source_file_left)
        target = pd.read_csv(target_file_right)
        prompt = "Press 'y' or 'n' if you wish to proceed or stop.\n"
        left_difference_con_num = len(source.columns)-len(target.columns)
        right_difference_con_num = len(target.columns) - len(source.columns)
        source_difference = set(source.columns.difference(target.columns))
        target_difference = set(target.columns.difference(source.columns))
        if set(source.columns) != set(target.columns) and 0 in {left_difference_con_num, right_difference_con_num}:
            print(f"Columns are different between {source_file_left} and {target_file_right}\n"
                f"{source_file_left}: \033[1m{tuple(source.columns)}\033[0m\n{target_file_right}: \033[1m{tuple(target.columns)}\033[0m")
            function = input(prompt).lower().strip()
            if function == 'n':
                exit()
        elif left_difference_con_num > 0:
            print(f"{source_file_left} file has these {left_difference_con_num} more column/s {source_difference} than {target_file_right}")
            function = input(prompt).lower().strip()
            if function == 'n':
                exit()
        elif right_difference_con_num:
            print(f"{target_file_right} file has these {right_difference_con_num} more column/s {target_difference} than {source_file_left}")
            function = input(prompt).lower().strip()
            if function == 'n':
                exit()
        number = '1'
        print(f"🕑 Comparing the {source_file_left} and {target_file_right} files")
        diff = pd.merge(source, target, how="outer", indicator="Exist")
        diff = diff.loc[diff["Exist"] == "left_only"]
        diff.drop(labels="Exist", axis="columns", inplace=True)
        diff.to_csv(path_or_buf=f"{result_file}", encoding='utf-8', index=False, header=True)
        for i in range(10):
            while exists(result_file):
                result_file = 'output' + '_' + date + '_' + number + '.csv'
                number = chr(ord(number) + 1)
            else:
                diff.to_csv(path_or_buf=f"{result_file}", encoding='utf-8', index=False, header=True)
                break
        if diff.empty:
            print("✅ Comparison file generated successfully with NO mismatches")
        elif len(diff.index) == 1:
            print(f"❌ Comparison file generated successfully with {len(diff.index)} mismatch")
        else:
            print(f"❌ Comparison file generated successfully with {len(diff.index)} mismatches")

    except Exception as e:
        tb = traceback.extract_tb((e.__traceback__))
        first_frame = tb[-1]
        print(f"❌ The following Exception occurred, line {first_frame.lineno} in {first_frame.name}\n{e}")


csv_comparison(source_file_left=f"2026-02-18_top_250_imdb_movies.csv", target_file_right=f"2026-02-03_top_250_imdb_movies.csv",
               result_file=f"output_{date}.csv")
