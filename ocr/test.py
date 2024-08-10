import requests

url = 'http://localhost:5000/api/extract_text'
files = {'image': open('note.jpg', 'rb')}

response = requests.post(url, files=files)
print(response.json())
