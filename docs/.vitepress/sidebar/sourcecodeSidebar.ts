import { DefaultTheme } from "vitepress";

export const sourcecodeSidebar: DefaultTheme.Sidebar = [
    {
        text: "seata",
        base: "/项目实战/seata",
        items: [
            { text: "seata的安装", link: "/seata的安装" },
            { text: "分布式事务TCC模式", link: "/分布式事务tcc模式" },
        ],
    },
    {
        text: "rabbitmq",
        base: "/项目实战/rabbitmq",
        items: [
            { text: "安装rabbitmq", link: "/安装" },
            { text: "基础", link: "/基础使用" },
            { text: "高阶", link: "/高阶使用" },
        ],
    },
];
