import requests  # http requests
from bs4 import BeautifulSoup  # web scraping
import smtplib  # Send mail
import os  # get from .env file
from dotenv import load_dotenv  # load .env file
import sys  # used only for the Schedule option, otherwise can be commented out
# email body
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import datetime  # system date and time
import re  # regex used to find the correct substring

# Current date used for the email summary
now = datetime.datetime.now().replace(microsecond=0)

# user agent required to establish the connection to the webpage API
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                  "AppleWebKit/537.36 (KHTML, like Gecko) "
                  "Chrome/120.0.0.0 Safari/537.36"
}


class ThermoenergeticIssue:
    def __init__(self, url, street_name):  # initialization of the url and street name parameters
        self.url = url
        self.street_name = street_name

    # function that handles the address extraction
    def extract_issue(self):
        print(f"✅ Extracting the Thermoenergetic issue for the {self.street_name} street")  # print to keep the user informed throughout the process
        cont = ''  # placeholder for the mail contents inside this function
        cont += ('<b>Affected Street:</b>\n'+'<br>'+'-'*80+'<br>')  # first line in the email if the street is present on the webpage
        response = requests.get(self.url, headers=headers)  # call to the site API to approve the connection
        content = response.content  # content of the webpage
        filters = BeautifulSoup(content, "html.parser")  # function that reads html content of the webpage
        address = re.compile(fr"({self.street_name} - \s*.+?)(?:\s*•|\s*Deficienta)")  # search for the specified street using a regex that takes into account addresses at the start/middle/end of the cell
        for i, tag in enumerate(filters.find("div", id="ST")):  # for loop to narrow down the search in the html body
            for f, adr in enumerate(address.findall(tag.text)):  # for loop to check each line for the street name
                cont += ((adr + '\n' + '<br>') if self.street_name in tag.text else '')  # the address/addresses is added to the contents of the email
        return cont  # returning the contents for the email function

    # function that generates and sends the email
    def email_generation(self):
        try:
            cont = self.extract_issue()  # call to the function that handles the address extraction
            content = ''  # email placeholder inside this function

            msg = MIMEMultipart()  # Call to function needed for the email Subject, From, To

            if self.street_name in cont:  # Check to see if the street is present in the webpage
                msg['Subject'] = f'[Automated Email] Addresses with a Thermoenergetic defect for {self.street_name}'   # email Subject if the street is present in the webpage
                content += cont  # the contents from the address extraction are added to the value inside this function
                content += ('-' * 80 + '<br>')  # line to make the email look more professional
            else:
                msg['Subject'] = f'[Automated Email] No Thermoenergetic defect for the {self.street_name} street'  # Replacement of the email Subject if the street is not present on the webpage
                content = (('<br>' + '-'*80 + '<br>') +
                           f'No Thermoenergetic defect has been reported for the {self.street_name} street at {now}'
                           + ('<br>' + '-'*80 + '<br>'))  # Replacement of the Mail body if the street is not present on the webpage

            # For the connection to gmail using a third party app, the 2-Factor Authentication needs to be set up for that gmail account
            # After that search App Password in the Search Google Account section and once selected type the App name (ex.Python) then press Create
            load_dotenv()  # call to load the .env file
            SERVER = os.getenv("SERVER")  # the smtp server from the .env file set to gmail as the default value
            PORT = os.getenv("PORT")  # port number from the .env file set to a default value
            FROM = os.getenv("FROM_EMAIL")  # From email from the .env file that needs to be set not having a default value
            TO = os.getenv("TO_EMAIL")  # To email from the .env file that needs to be set not having a default value
            PASSWORD = os.getenv("EMAIL_PASSWORD")  # Password for the From email located in the .env file that needs to be set not having a default value

            msg['From'] = FROM  # assigning the FROM mail in .env to the MIME variable
            msg['To'] = TO  # assigning the TO mail in .env to the MIME variable
            print("✅ Composing Email")  # print to keep the user informed throughout the process

            msg.attach(MIMEText(content, 'html'))  # attaching the body of the Email to the accompanying Subject, From, To

            print('✅ Initiating Connection')  # print to keep the user informed throughout the process

            server = smtplib.SMTP(SERVER, PORT)  # function to connect to the email server using the port
            server.set_debuglevel(0)  # setting the debug level to 0 to not show any traceback messages
            server.ehlo()  # initiating command for the connection
            server.starttls()  # starting the secured connection
            server.login(FROM, PASSWORD)  # login using the FROM email and PASSWORD from the .env file
            server.sendmail(FROM, TO, msg.as_string())  # sending one of the two composed emails based on the presence of the street in the webpage

            print("✅ Email Sent")  # print to keep the user informed throughout the process

            server.quit()  # closing the connection to the server

        except Exception as e:  # Exception handling
            print(f"❌ The following Exception occurred:\n{e}")  # print of the exception and the line that triggered it

            return self


# ONLY NEEDED IF THE SCHEDULE OPTION IS IMPLEMENTED OTHERWISE COMMENT ALONG WITH THE IMPORT
street_name = sys.argv[2]  # Argument when calling the function from a related  app (ex.cmd)

# Call to the class to generate the email, the street_name=street_name can be replaced if the Schedule option is not selected with ex.street_name="Moșilor"
ThermoenergeticIssue(url="https://cmteb.ro/functionare_sistem_termoficare.php", street_name=street_name).email_generation()
