from flask import Flask, request, jsonify
import matplotlib.pyplot as plt
import io
from PIL import Image
import pytesseract

app = Flask(__name__)

@app.route('/api/extract_text', methods=['POST'])
def extract_text():
    try:
        image_file = request.files['image']
        image = Image.open(image_file)
            
        # plt.imshow(image)
        # plt.axis('off')
        # plt.show()
        if image_file:
            extracted_text = pytesseract.image_to_string(image)
            return jsonify({'text': extracted_text})
        else:
            return jsonify({'error': 'No image file provided'}), 400
    except Exception as e:
        return jsonify({'error': str(e)}), 500
@app.route("/", methods=["GET"])
def home():
    return "Hello"
if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0")
