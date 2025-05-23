import { useEditor } from '@/stores/editor'

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
      || key === 'largeImageKey' || key === 'smallImageKey' || key === 'allowedChannels') {
        target.push('')
    }

    if (key === 'entries') {
        const filterEntry = useEditor().getConfig.config.general.configVersion > 26 ? {
            search: "",
            target: "CHAT",
            replace: "",
            searchMode: "CONTAINS",
            action: "IGNORE",
            appliesTo: "DISCORD",
            ignoreConsole: false
        } : {
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

    if (key === 'Buttons') {
        const stardewButton = {
            Title: "",
            Url: ""
        }

        target.push(stardewButton);
    }

    if (key === 'variables') {
        const mcVariable = {
            name: '',
            value: ''
        }

        target.push(mcVariable)
    }

    if (key === 'Variables') {
        const stardewVariable = {
            Name: '',
            Value: ''
        }

        target.push(stardewVariable)
    }

    if (key === 'dimensions') {
        if (useEditor().getConfig.config.general.version < 25) {
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
        } else {
            const mcDim = {
                name: '',
                presence: []
            }

            target.push(mcDim);
        }
    }

    if (key === 'roleAdded' || key === 'roleRemoved') {
        const roleAdded = {
            discordRole: '',
            minecraftCommand: []
        }

        target.push(roleAdded);
    }

    if (key === 'presence') {
        const presence = {
            type: "PLAYING",
            description: "",
            state: "",
            largeImageKey: [],
            largeImageText: "",
            smallImageKey: [],
            smallImageText: "",
            streamingActivityUrl: "https://twitch.tv/twitch",
            buttons: []
        }

        target.push(presence)
    }

    if (key === 'botStatus') {
        const botStatus = {
            status: '',
            botStatusType: 'CUSTOM_STATUS',
            botStatusStreamingURL: 'https://twitch.tv/twitch'
        }

        target.push(botStatus);
    }

    if (key === 'syncs') {
        const sync = {
            rank: '',
            role: ''
        }

        target.push(sync);
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