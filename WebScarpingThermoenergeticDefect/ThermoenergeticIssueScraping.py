import requests # http requests
from bs4 import BeautifulSoup # web scraping
import smtplib # Send mail
import os # get from .env file
from dotenv import load_dotenv # load .env file
import sys # used only for Schedule
# email body
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import datetime # system date and time
import re # regex used to find the correct substring

now = datetime.datetime.now().replace(microsecond=0)
date = str(now.date())

headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                  "AppleWebKit/537.36 (KHTML, like Gecko) "
                  "Chrome/120.0.0.0 Safari/537.36"
}


class ThermoenergeticIssue:
    def __init__(self, url, street_name):
        self.url = url
        self.street_name = street_name

    def extract_issue(self):
        print(f"Extracting the Thermoenergetic issue for the {self.street_name} street")
        cont = ''
        cont += ('<b>Affected Street:</b>\n'+'<br>'+'-'*80+'<br>')
        response = requests.get(self.url, headers=headers)
        content = response.content
        filters = BeautifulSoup(content, "html.parser")
        address = re.compile(fr"({self.street_name} - \s*.+?)(?:\s*•|\s*Deficienta)")
        for i, tag in enumerate(filters.find("div", id="ST")):
            for f, adr in enumerate(address.findall(tag.text)):
                cont += ((adr + '\n' + '<br>') if self.street_name in tag.text else '')
        return cont

    def email_generation(self):
        try:
            cont = self.extract_issue()
            content = '' # email placeholder
            content += cont
            content += ('-'*80 + '<br>')

            msg = MIMEMultipart()

            if self.street_name not in cont:
                msg['Subject'] = f'[Automated Email] No Thermoenergetic issue for the {self.street_name} street'
                content = (('<br>' + '-'*80 + '<br>') +
                           f'No Thermoenergetic issue have been reported for the {self.street_name} street at {now}'
                           + ('<br>' + '-'*80 + '<br>'))
            else:
                msg['Subject'] = f'[Automated Email] Addresses with a Thermoenergetic defect for {self.street_name}'

            load_dotenv()
            SERVER = os.getenv("SERVER")# the smtp server
            PORT = os.getenv("PORT")  # port number
            FROM = os.getenv("FROM_EMAIL")
            TO = os.getenv("TO_EMAIL")
            PASSWORD = os.getenv("EMAIL_PASSWORD")

            msg['From'] = FROM
            msg['To'] = TO
            print("Composing Email")

            msg.attach(MIMEText(content, 'html'))

            print('Initiating Connection')

            server = smtplib.SMTP(SERVER, PORT)
            server.set_debuglevel(0)
            server.ehlo()
            server.starttls()
            server.login(FROM, PASSWORD)
            server.sendmail(FROM, TO, msg.as_string())

            print("Email Sent")

            server.quit()

        except Exception as e:  # Exception handling
            print(f"❌ The following Exception occurred:\n{e}")  # print of the exception and the line that triggered it

            return self


street_name = sys.argv[2]

ThermoenergeticIssue(url="https://cmteb.ro/functionare_sistem_termoficare.php", street_name=street_name).email_generation()
