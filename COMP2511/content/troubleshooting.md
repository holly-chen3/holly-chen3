# Java / VSCode / Gradle Setup Troubleshooting

## VSCode Setup

Most of the steps are to do with imports not being resolved, though (1) and (2)
 are good as a general go-to.
 
1. Before you do anything else - **make sure you have the repository folder itself open in VSCode and not a subfolder or a parent folder**. For example if the repo is `lab01` you should see `LAB01` at the top of the project directory menu on the left panel (see the screenshot below). To open a particular folder go to File > Open and then select the folder you are working with.  
<img src="https://static.au.edusercontent.com/files/TMhoLTB1SpWtWMZ7kpjIb63c" width="300" />

2. If your VSCode is acting strangely, everything else should be working fine you can refresh everything by going **View: Command Pallete > Java: Clean the Java language workspace** and then click the button that says **Restart and Delete**.
3. Make sure the `src` and the `lib` folders are on the classpath. In VSCode your `.vscode/settings.json` should look like this:

```javascript
{
    "java.project.sourcePaths": [
        "src"
    ],
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ]
}
```

You can add/remove folders to the Java Source Path by right clicking on them and clicking **Add/Remove Folder to Java Source Path**.

4. Run/Debug button not showing above `public static void main(String[] args)`. Check you have the **Extension Pack for Java** extension for VSCode installed. To verify this, if you switch to the extension tab in VSCode and search for Java it should show at least the following extensions installed.

<img src="https://static.au.edusercontent.com/files/Nbr1l1phCjvZtK6fzxu33xRp" width="500" />

5. If the `org.json` package isn't being imported - try uninstalling the "Java Language Support" extension. See this reference here: https://github.com/redhat-developer/vscode-java/issues/956

6. In the left panel under your directory structure, find the drop-down menu that says **Java Projects** and expand it. Check the `lib` folder contains all of your packages, and if not press the `+` and select the relevant files from inside the `lib` folder.

<img src="https://static.au.edusercontent.com/files/d9JJA6p7k8ZcbCd4AgTl4yn1" width="200" />

7. If you are still running into problems, try cleaning the workspace data for VSCode manually (delete the contents of) `$HOME/Library/Application Support/Code/workspaceStorage`. This is what the Restart & Delete button does but that's how you can do it yourself.

