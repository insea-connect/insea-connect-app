const Placeholder = () => {
  return (
    <div className="flex px-2 flex-col gap-5 items-center justify-center w-full">
      <span className="text-9xl">🎨</span>
      <h2 className="text-4xl text-center lg:text-5xl font-extrabold">
        <span className="bg-primary dark:text-background text-white px-1">
          Artist
        </span>{" "}
        Detected!
      </h2>
      <p className=" text-base lg:text-xl text-center ">
        Your creative skills are just what this feature needs. Paint your legacy
        <a
          href="https://github.com/insea-connect/insea-connect-app"
          target="_blank"
          className="text-primary hover:text-primary/80 pl-2 font-bold"
        >
          here!
        </a>
      </p>
    </div>
  );
};

export default Placeholder;
