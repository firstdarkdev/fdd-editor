
export const addToArray = (target: any, key: any) => {
    if (key === 'permissions') {
        const mcCmd = {
            role: "0",
            commands: [],
            permissionLevel: 1
        }

        target.push(mcCmd);
    }

    if (key === 'requiredRoles' || key === 'deniedRoles' || key === 'ignoredCommands' || key === 'ignoredThreads'
      || key === 'largeImageKey' || key === 'smallImageKey') {
        target.push("")
    }

    if (key === 'entries') {
        const filterEntry = {
            search: "",
            replace: "",
            searchMode: "CONTAINS",
            action: "IGNORE"
        }

        target.push(filterEntry);
    }

    if (key === 'buttons') {
        const mcButton = {
            label: "",
            url: ""
        }

        target.push(mcButton);
    }

    if (key === 'variables') {
        const mcVariable = {
            name: '',
            value: ''
        }

        target.push(mcVariable)
    }

    if (key === 'dimensions') {
        const mcDim = {
            name: '',
            description: '',
            state: '',
            largeImageKey: [],
            largeImageText: '',
            smallImageKey: [],
            smallImageText: '',
            buttons: []
        }

        target.push(mcDim);
    }
}

export const headerToDisplay = (indentifier: any) => {
    if (indentifier === undefined)
        return '';

    const words = indentifier.match(/([A-Z]+(?=[A-Z][a-z])|[A-Z]?[a-z]+)/g) || [];
    // @ts-ignore
    const capitalizedWords = words.map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase());
    return capitalizedWords.join(' ');
}