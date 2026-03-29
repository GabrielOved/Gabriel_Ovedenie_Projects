from selenium import webdriver  # Selenium driver used by ChromeDriver or GeckoDriver (Firefox), Edge WebDriver, SafariDriver, OperaDriver
from selenium.webdriver.chrome.service import Service  # class that manages the webdriver
import requests  # used to download the file from the link obtained by Selenium
import glob  # used to filter files in directory by a certain extension in this case: .apk
import os  # used to order the files with an extension in descending order
from os.path import exists  # used to verify if the file downloaded successfully


def SeleniumDownloadAPK(url, version=1):
    try:
        chrome_executable_path = "C:\\Users\\USER_NAME\\PATH_TO_EXE\\chrome-win64\\chrome.exe"  # path to the web driver browser executable
        chrome_driver_path = "C:\\Users\\USER_NAME\\PATH_TO_EXE\\chromedriver-win64\\chromedriver.exe"  # path to the web driver executable
        folder_path = "C:\\Users\\USER_NAME\\PATH_TO\\DESTINATION"  # path to the download destination

        chrome_options = webdriver.ChromeOptions()  # options for the webdriver to use the browser for testing, not the installed browser
        chrome_options.binary_location = chrome_executable_path  # driver options is assigned the browser for testing executable

        chrome_service = Service(executable_path=chrome_driver_path)  # driver service is assigned the browser for testing executable
        chrome_driver = webdriver.Chrome(service=chrome_service, options=chrome_options)  # webdriver is assigned the browser for testing and driver used by the browser

        chrome_driver.get(url)  # driver opens the url from the variable
        # driver finds the element or elements if there are multiple from the xpath
        download_links = chrome_driver.find_elements(by='xpath', value='//p[@class="package-version-download"]/b/a')
        for i, file_version in enumerate(download_links):  # for loop to cycle through each element
            print("Available version:", i+1, "|", file_version.get_attribute("href").split("/")[-1])  # print to display all the available versions for download
        file_url = download_links[version-1].get_attribute("href")  # variable assigned the link of only the specified version (incremented +1 as to not have the first version be 0)
        filename = file_url.split("/")[-1]  # variable with only the file name and extension
        filename_without_extension = filename.rsplit(".", 1)[0]  # variable with only the filename

        chrome_driver.switch_to.new_window("tab")   # opening new tab to show file progress as it is not tied to Selenium
        # JavaScript that displays text in the new tab (text is dark green bold with a font size of 30 pixels)
        chrome_driver.execute_script("""
            let div = document.createElement('div');
            div.id = 'progress';
            div.style.fontSize = '30px';
            div.style.color = 'darkgreen';
            div.style.fontWeight = "bold";
            div.innerText = 'Starting download....';
            document.body.innerHTML = '';
            document.body.appendChild(div);
        """)

        file_extension = "*.apk"  # variable with the file extension for searching inside the destination directory
        file_path = os.path.join(folder_path, file_extension)  # join for the folder path and file extension
        files_with_extension = sorted(glob.iglob(file_path), key=os.path.getctime, reverse=True)  # sorting of the files with the specified extension in descending order
        # check if the file that is going to be downloaded is already present in the specified directory
        if not any(
                filename_without_extension in file.split("\\")[-1]
                for file in files_with_extension
        ):
            response = requests.get(file_url, allow_redirects=True)  # request to download from the url link
            total_download = int(response.headers.get("content-length", 0))  # total size of the file being downloaded
            download_update_interval = 500_000  # 500 KB
            downloaded = 0  # variable to keep track of how much was downloaded
            download_last_update = 0  # variable to keep track of each download update
            with open(f"{folder_path}\\{filename}", "wb") as f:  # creation of the download file in the specified directory
                for download_chunk in response.iter_content(chunk_size=8192):  # for loop of the response content in chunks of 8192 bytes (8 KB)
                    if download_chunk:  # only process if this chunk is not empty
                        f.write(download_chunk)  # write the current chunk to the file
                        downloaded += len(download_chunk)  # variable to keep track of total bytes downloaded so far
                        # check if enough bytes have been downloaded since the last update
                        if downloaded - download_last_update > download_update_interval:
                            download_percent = int(downloaded * 100 / total_download) if total_download else 0  # variable to calculate the current download percentage
                            # update the progress message in the browser via Selenium
                            chrome_driver.execute_script(f"""
                                document.getElementById('progress').innerText =
                                'Downloading.... {download_percent}%';
                            """)
                            download_last_update = downloaded  # update the last marker to the current downloaded size
            if exists(f"{folder_path}\\{filename}"):  # check if the file downloaded successfully
                print(f"✅ The \033[1m{filename}\033[0m downloaded successfully.")  # print to inform the user that file downloaded

        else:  # else to notify the user that the file was downloaded previously only if it is present in the specified directory
            print(f"⚠️ The \033[1m{filename}\033[0m has already been downloaded")

    except Exception as e:  # Exception handling
        print(f"❌ The following Exception occurred:\n{e}")  # print of the exception and the line that triggered it
    finally:  # finally block that closes the driver even if an exception occurs
        if chrome_driver:
            chrome_driver.quit()


# Call to the function to download the specified version of the file from the specified link
SeleniumDownloadAPK(url="https://f-droid.org/packages/net.osmand.plus/", version=1)
