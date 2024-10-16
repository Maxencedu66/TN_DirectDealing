import os

dir_List = ["./java/eu/telecomnancy/labfx/", "./resources/eu/telecomnancy/labfx/"]

first = True

for dir in dir_List:
    if first:
        os.system("echo 'Le dossier " + dir + " contient les fichiers suivants:\n' > all.txt")
        first = False
    else:
        os.system("echo 'Le dossier " + dir + " contient les fichiers suivants:\n' >> all.txt")
    os.system("ls " + dir + " >> all.txt")
    os.system("echo '\n\nLe contenu des fichiers est le suivant:\n' >> all.txt")
    os.system("cat " + dir + "/*.java >> all.txt\n\n")
    os.system("cat " + dir + "/*.fxml >> all.txt\n\n")
    print("Succes")
