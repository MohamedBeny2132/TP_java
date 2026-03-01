import sys

try:
    with open('boot_app_2.log', 'rb') as f:
        content = f.read()
    
    # Try different encodings
    for encoding in ['utf-16', 'utf-8', 'cp1252']:
        try:
            text = content.decode(encoding)
            print(f"--- Decoded with {encoding} ---")
            print(text)
            break
        except:
            continue
except Exception as e:
    print(f"Error: {e}")
