with open("boot_error.log", "r", encoding="utf-16le") as f:
    lines = f.readlines()
with open("boot_error_utf8.log", "w", encoding="utf-8") as f:
    f.writelines(lines[-250:])
